package eu.gaiax.difs.aas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.gaiax.difs.aas.client.InvitationServiceClient;
import eu.gaiax.difs.aas.client.RestInvitationServiceClientImpl;
import eu.gaiax.difs.aas.properties.StatusProperties;

@Configuration
public class InvitationServiceConfig {

    @Bean
    @Profile("prod")
    public InvitationServiceClient restInvitationServiceClient() {
        return new RestInvitationServiceClientImpl();
    }

    @Bean
    @Profile("!prod")
    public InvitationServiceClient localInvitationServiceClient(StatusProperties statusProperties) {
        return new RestInvitationServiceClientImpl();
    }

}