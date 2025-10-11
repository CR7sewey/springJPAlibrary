package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component // - se ativo nao o tinha de passar no security pq estava no contexto do spring ja, entao ja achava o Registered client
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {
// authorization server validar se esta registado e foi passada a credencial correta
/*
The RegisteredClientRepository is the central component where new clients can be registered and existing clients can be queried.
It is used by other components when following a specific protocol flow, such as client authentication, authorization grant processing, token introspection, dynamic client registration, and others.

The provided implementations of RegisteredClientRepository are InMemoryRegisteredClientRepository and JdbcRegisteredClientRepository.
The InMemoryRegisteredClientRepository implementation stores RegisteredClient instances in-memory and is recommended ONLY to be used during development and testing. JdbcRegisteredClientRepository is a JDBC implementation that persists RegisteredClient instances by using JdbcOperations.
 */
    private final ClientService  clientService;
    private final TokenSettings  tokenSettings;
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = clientService.findByClientId(clientId);
        if (client != null) {
            return RegisteredClient // crias o RegisteredClient para enviar para o AuthorizarionServer
                    .withId(client.getId().toString())
                    .clientId(client.getClientId())
                    .clientSecret(client.getClientSecret())
                    .scope(client.getScope())
                    .redirectUri(client.getRedirectUri())
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // passamos a info na Basic Auth, se Post podemos passar via header
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // tipo de grant type (ver esquema ReadMe)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // used with authorization_code ou login com user e senha
                    .tokenSettings(tokenSettings)
                    .clientSettings(clientSettings)
                    .build();
        }
        return null;
    }
}
