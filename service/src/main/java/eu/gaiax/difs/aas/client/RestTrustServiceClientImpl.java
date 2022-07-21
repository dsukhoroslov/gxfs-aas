package eu.gaiax.difs.aas.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.web.reactive.function.client.WebClient;

import eu.gaiax.difs.aas.generated.model.AccessRequestStatusDto;
import reactor.core.publisher.Flux;

public class RestTrustServiceClientImpl implements TrustServiceClient {

    private static final Logger log = LoggerFactory.getLogger(RestTrustServiceClientImpl.class);
    private static final Logger claims_log = LoggerFactory.getLogger("tsclaims");

    private final WebClient client;
    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF = new ParameterizedTypeReference<>() {
    };

    @Value("${aas.tsa.url}")
    private String url;
    @Value("${aas.tsa.repo}")
    private String repo;
    @Value("${aas.tsa.group}")
    private String group;
    @Value("${aas.tsa.version}")
    private String version;
    @Value("${aas.tsa.action}")
    private String action;

    public RestTrustServiceClientImpl() {
        client = WebClient.builder().baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Map<String, Object> evaluate(String policy, Map<String, Object> params) {
        log.debug("evaluate.enter; got policy: {}, params: {}", policy, params);
        claims_log.debug("evaluate.enter; got policy: {}, params: {}", policy, params);
        //String uri = "/{repo}/policies/{group}/{policyname}/{version}/{action}";
        String uri = "/policy/{group}/{policyName}/{version}/{action}";
        // baseUrl doesn't work for some reason, so I specify it here
        Flux<Map<String, Object>> trustServiceResponse = client.post().uri(url, uriBuilder -> 
                    uriBuilder.path(uri).build(/*repo,*/ group, policy, version, action)) 
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(params)
                .retrieve()
                .bodyToFlux(MAP_TYPE_REF);
        Map<String, Object> result = trustServiceResponse.blockFirst();
        claims_log.debug("evaluate; got claims: {}", result);
        String sts = (String) result.get(PN_STATUS);
        AccessRequestStatusDto status;
        if (sts == null) {
            status = AccessRequestStatusDto.PENDING;
        } else {
            try {
                status = AccessRequestStatusDto.valueOf(sts);
            } catch (Exception ex) {
                log.info("evaluate.error; got unexpected status: {}", sts);
                status = AccessRequestStatusDto.REJECTED;
            }
        }
        result.put(PN_STATUS, status);
        result.remove(IdTokenClaimNames.SUB);
        result.remove(IdTokenClaimNames.ISS);
        result.remove(IdTokenClaimNames.AUTH_TIME);
        String requestId = (String) result.get(PN_REQUEST_ID);
        if (requestId == null) {
            requestId = (String) params.get(PN_REQUEST_ID);
            // a quick fix for TSA mock..
            result.put(IdTokenClaimNames.SUB, requestId);
        }
        log.debug("evaluate.exit; returning claims: {}", result.size());
        return result;
    }
    
}
