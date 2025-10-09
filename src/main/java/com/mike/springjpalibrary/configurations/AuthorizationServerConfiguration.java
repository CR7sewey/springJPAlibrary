package com.mike.springjpalibrary.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    public TokenSettings tokenSettings() {
        HashMap<String, Object> settings = new HashMap<String, Object>();
        //settings.put("token_signing_algorithm", "HS256");
        settings.put("token_expiration", Duration.ofMinutes(60)); // 1 h
        settings.put("access_token_format", OAuth2TokenFormat.SELF_CONTAINED);

        return TokenSettings
                .withSettings(settings)
                //.builder
                //.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                //.accessTokenTimeToLive(Duration.ofMinutes(60))
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings
                .builder()
                .requireAuthorizationConsent(false) // nao precisa do consentimento para autorizar infos
                .build();
    }


}
