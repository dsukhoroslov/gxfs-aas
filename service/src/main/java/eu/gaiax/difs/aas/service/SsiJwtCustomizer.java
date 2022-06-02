package eu.gaiax.difs.aas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SsiJwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Logger log = LoggerFactory.getLogger(SsiJwtCustomizer.class);

    @Autowired
    private  SsiBrokerService ssiBrokerService;

    @Override
    public void customize(JwtEncodingContext context) {
        log.debug("customize.enter; got context: {}", context);
        String requestId = getRequestId(context);
        
        boolean updated = false;
        if ("id_token".equals(context.getTokenType().getValue())) {
            OAuth2Authorization auth = context.get(OAuth2Authorization.class);
            OAuth2AuthorizationRequest oar = auth.getAttribute(OAuth2AuthorizationRequest.class.getName());
            Map<String, Object> additionalParams = oar.getAdditionalParameters();
            Map<String, Object> idToken = null;
            Object maxAge = null;
            if (additionalParams != null && !additionalParams.isEmpty()) {
                String sClaims = (String) additionalParams.get("claims");
                Map<String, Object> claims;
                if (sClaims != null) {
                    JacksonJsonParser jsonParser = new JacksonJsonParser();
                    claims = jsonParser.parseMap(sClaims);
                    idToken = (Map<String, Object>) claims.get("id_token");
                } else {
                    claims = new HashMap<>();
                }
                maxAge = additionalParams.get("max_age");
                if (maxAge != null) {
                    claims.put(IdTokenClaimNames.AUTH_TIME, 1);
                }
                updated = ssiBrokerService.setAdditionalParameters(requestId, claims);
            }
            
            Set<String> claims = idToken == null ? maxAge == null ? null : Set.of(IdTokenClaimNames.AUTH_TIME) : 
                maxAge == null ? idToken.keySet() : Stream.concat(idToken.keySet().stream(), Set.of(IdTokenClaimNames.AUTH_TIME).stream()).collect(Collectors.toSet()); 

            // the below is required in case when additional claims were requested via claims.id_token or max_age params
            if (claims != null) {
                Map<String, Object> userDetails = ssiBrokerService.getUserClaims(requestId, false, null, claims); // required?
                if (userDetails != null) {
                    for (Map.Entry<String, Object> e: userDetails.entrySet()) {
                        context.getClaims().claim(e.getKey(), e.getValue());
                    }
                }
            }
        }
        log.debug("customize.exit; updated claims: {} for request: {}", updated, requestId);
    }

    private String getRequestId(JwtEncodingContext context) {
        AtomicReference<String> requestId = new AtomicReference<>();
        context.getClaims().claims(claims -> requestId.set((String) claims.get(IdTokenClaimNames.SUB)));
        String id = requestId.get();
        if (id != null) {
            return new String(Base64.getDecoder().decode(id));
        }
        return null;
    }

}
