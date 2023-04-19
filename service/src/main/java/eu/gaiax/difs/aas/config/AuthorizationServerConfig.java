/*-
 * ---license-start
 * EU Digital Green Certificate Gateway Service / dgc-gateway
 * ---
 * Copyright (C) 2021 T-Systems International GmbH and all other contributors
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package eu.gaiax.difs.aas.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import eu.gaiax.difs.aas.properties.ScopeProperties;
import eu.gaiax.difs.aas.service.SsiAuthorizationService;
import eu.gaiax.difs.aas.service.SsiBrokerService;
import eu.gaiax.difs.aas.service.SsiClientRegistrationAuthProvider;
import eu.gaiax.difs.aas.service.SsiClientsRepository;
import eu.gaiax.difs.aas.service.SsiCorsConfigurationSource;
import eu.gaiax.difs.aas.service.SsiUserInfoAuthProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * The Spring Authorization Server config.
 */
@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Value("${aas.cache.size}")
    private int cacheSize;
    @Value("${aas.cache.ttl}")
    private Duration cacheTtl;
    @Value("${aas.oidc.issuer}")
    private String oidcIssuer;
    @Value("${aas.token.ttl}")
    private Duration tokenTtl;
    @Value("${aas.jwk.length}")
    private int jwkLength;
    @Value("${aas.jwk.secret}")
    private String jwkSecret;
    
    private final ScopeProperties scopeProperties;
    
    @Autowired
    public AuthorizationServerConfig(ScopeProperties scopeProperties) {
        this.scopeProperties = scopeProperties;
    }
    
	@Bean 
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http, SsiClientsRepository clientsRepo, SsiBrokerService ssiBroker) throws Exception {
    	log.debug("authorizationServerSecurityFilterChain.enter");
    	SsiCorsConfigurationSource corsSource = new SsiCorsConfigurationSource(clientsRepo);
		applyOidcSettings(http, corsSource, ssiBroker);
		
		http  
		    .cors()
		        .configurationSource(corsSource)
		    .and()
			// Redirect to the login page when not authenticated from the
			// authorization endpoint
			.exceptionHandling((exceptions) -> exceptions
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/ssi/login"))
			)
            .formLogin()
             //   .loginPage("/ssi/login")
            //    .loginProcessingUrl("/login")
            .and()    
			// Accept access tokens for User Info and/or Client Registration
			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
			;

    	log.debug("authorizationServerSecurityFilterChain.exit");
		return http.build();
	}   
	
    private void applyOidcSettings(HttpSecurity http, SsiCorsConfigurationSource corsSource, SsiBrokerService ssiBroker) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

   		//authorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint -> 
   		//	authorizationEndpoint.authenticationProvider(new SsiAuthProvider(ssiBroker))
   		//);
        
   		authorizationServerConfigurer.oidc(oidc -> 
			oidc.clientRegistrationEndpoint(clientRegistrationEndpoint -> 
				clientRegistrationEndpoint.authenticationProviders(providers -> {
					AuthenticationProvider proOne = providers.get(0);
					SsiClientRegistrationAuthProvider proSsi = new SsiClientRegistrationAuthProvider(corsSource, proOne);
					providers.set(0, proSsi);
				})
			)	
   		);

   		authorizationServerConfigurer.oidc(oidc -> 
   			oidc.providerConfigurationEndpoint(providerConfigurationEndpoint ->
   				providerConfigurationEndpoint
   					.providerConfigurationCustomizer(c ->
   						c.claims(claims()
   					)
   					.grantTypes(g -> {
   						g.clear();
   						g.add(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
   					})
   					.responseTypes(r -> {
   						r.clear();
   						r.add(OAuth2AuthorizationResponseType.CODE.getValue());
   					})
   					.scopes(s -> {
   						s.clear();
   						s.addAll(scopeProperties.getScopes().keySet());
   					})
   					.tokenEndpointAuthenticationMethods(m -> {
   						//m.clear();
   						m.add(ClientAuthenticationMethod.NONE.getValue());
   					}) 
   				) 
   			)
   		);
        
   		authorizationServerConfigurer.oidc(oidc ->
   			oidc.userInfoEndpoint(userInfoEndpoint ->
			    userInfoEndpoint.authenticationProvider(new SsiUserInfoAuthProvider(ssiBroker)) 
			    // .userInfoMapper(ctx -> userInfo(ctx))
			)
   		);
   		
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer)
                .and()
                .addFilterAfter(new SsiOAuth2ValidationFilter(), LogoutFilter.class)
                ;
    }
    
    private Consumer<Map<String, Object>> claims() {
        List<String> supportedClaims = scopeProperties.getScopes()
                .values().stream().flatMap(List::stream)
                .distinct().sorted()
                .collect(Collectors.toList());

        return (claims) -> {
            claims.put("userinfo_signing_alg_values_supported", List.of("RS256"));
            claims.put("display_values_supported", List.of("page"));
            claims.put("claims_supported", supportedClaims);
            claims.put("claims_locales_supported", List.of("en"));
            claims.put("ui_locales_supported", List.of("en", "de", "fr", "ru", "sk"));
            claims.put("end_session_endpoint", oidcIssuer + "/logout");
        };
    }
        
    private OidcUserInfo userInfo(OidcUserInfoAuthenticationContext context) {
    	log.debug("userInfo.enter; got context: {}", context);
        OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
        JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
    	OidcUserInfo info = new OidcUserInfo(principal.getToken().getClaims());
    	log.debug("userInfo.exit; returning: {}", info);
    	return info;
    }

    @Bean
    public SsiClientsRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
    	return new SsiClientsRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(oidcIssuer)
                .build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) throws JOSEException {
        JWK jwk = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null).get(0);
        OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefaultWithIssuer(oidcIssuer);
        RSAPublicKey publicKey = jwk.toRSAKey().toRSAPublicKey();
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        jwtDecoder.setJwtValidator(jwtValidator);
        return jwtDecoder;
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(jwkSecret)
                .build();
    }

    private KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(jwkLength);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new SsiAuthorizationService(cacheSize, cacheTtl);
    }
    
    //@Bean
    //public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
    //    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    //}
        
}

