package com.mike.springjpalibrary.configurations;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1) // dentro da cadeia de filtros do spring security ele fica como o primeiro
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // habilitaar AuthorizationServer
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

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
                //.withSettings(settings)
                .builder()
                // access_token: used token to perform requisitions - jwt token
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                // refresh_token: used to renovate the access_token - opaco token
                .reuseRefreshTokens(true)
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings
                .builder()
                .requireAuthorizationConsent(false) // nao precisa do consentimento para autorizar infos
                .build();
    }

    // gerar token jwk -token signature
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAKey rsaKey = generateRSA();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }


    // criptografia de chave (chave pub (criptografar dados) e private (desencriptar dados))
    private RSAKey generateRSA() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // btis
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(
                rsaPublicKey
        ).privateKey(rsaPrivateKey).keyID(UUID.randomUUID().toString()).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings
                .builder()
                .tokenEndpoint("/oauth2/token") // url to get a token
                .tokenIntrospectionEndpoint("/oauth2/token_introspection") // info about the token (Bearer ${token}) via header
                .tokenRevocationEndpoint("/oauth2/token_revocation") // revogar o token - POST
                .authorizationEndpoint("/oauth2/authorize") // to get the authorization code (authorization server) - reencaminha oara form login
                .oidcUserInfoEndpoint("oauth2/userinfo") // info do user OPEN ID CONNECT - to be implemented
                .jwkSetEndpoint("/oauth2/jwks") // get public keys to validate token signature
                .oidcLogoutEndpoint("/oauth2/logout") // logout
                .build();
    }


}
