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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import eu.gaiax.difs.aas.service.SsiAuthProvider;
import eu.gaiax.difs.aas.service.SsiJwtCustomizer;

/**
 * The Spring Security config.
 */
@EnableWebSecurity //(debug = true)
public class SecurityConfig {

    private final String[] ANT_MATCHERS = {
            "/api/**",
            "/swagger-ui/**",
//            "/login",
//            "/error",
            "/actuator",
            "/actuator/**",
            "/**/*.{js,html,css}",
            // "/oauth2/**",
            "/.well-known/**",
            "/ssi/**",
            "/clients/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(ANT_MATCHERS)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .failureHandler(new AuthenticationFailureHandler() {
                    
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException exception) throws IOException, ServletException {
                        String error = exception.getMessage();
                        if (error == null && exception instanceof OAuth2AuthenticationException) {
                            error = ((OAuth2AuthenticationException) exception).getError().getErrorCode();
                        }
                        request.getSession().setAttribute("AUTH_ERROR", error);
                        String redirectUrl = request.getContextPath() + "/ssi/login";
                        response.sendRedirect(redirectUrl);
                    }
                });
        return http.build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        return new SsiAuthProvider();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return new SsiJwtCustomizer();
    }

}
