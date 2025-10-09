package com.mike.springjpalibrary.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1) // dentro da cadeia de filtros do spring security ele fica como o primeiro
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // habilitaar AuthorizationServer

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // infos do token para front
        http.oauth2ResourceServer(oauth2ResourceServer -> {
            oauth2ResourceServer.jwt(Customizer.withDefaults()); // valida tokens gerados por o resourc server e sendo usados noutras apps
        });

        http.formLogin(configurer -> configurer.loginPage("/login").permitAll());

        return http.build();

    };

    @Bean //
    public PasswordEncoder passwordEncoder() { // password encoder interface
        return new BCryptPasswordEncoder(10);
    }

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
