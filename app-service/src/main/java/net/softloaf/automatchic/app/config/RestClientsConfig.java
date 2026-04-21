package net.softloaf.automatchic.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {
    @Bean
    public RestClient historyClient() {
        return RestClient.builder()
                .baseUrl("http://history:8080/api/v1/progress")
                .build();
    }
}
