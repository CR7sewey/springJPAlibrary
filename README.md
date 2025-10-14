# Authorization Server

[Link]: https://docs.spring.io/spring-authorization-server/reference/core-model-components.html

Como está na previous branch, temos a authentificação via Basic Http (username e password) e Login Social (que usa a api OAuth2 da google).

O objetivo aqui foi implementar algo semelhante, isto é, o protocolo OAuth2. O flow básico de authentificação seria: User acessa o sistema (client), é redirecionado para o authorization server, solicita a authentificação e faz o login. O authorization server emite um token ao client, permitindo assim o acesso à API (Resource Server).

<img width="794" height="512" alt="Screenshot 2025-10-13 165106" src="https://github.com/user-attachments/assets/c1557480-e856-4694-8acb-36977082cae8" />
<img width="562" height="682" alt="Screenshot 2025-10-13 165151" src="https://github.com/user-attachments/assets/f24f76d6-ed2e-4fc4-9104-d1e0cec1f823" />

Essencialmente, para fazer a authentificação, temos de definir um RegisteredClient. Este é o equivalente a um UserDetails, isto é, a partir, que será injetado no contexto do spring security, poderemos aceder e formar o server. Parametros principais são clientId (id), clientSecret (pass), redirectUri (para onde será enviado o code no step 1), scope (escopo de atuação - aka authorities). Implementado nas classes RegisteredClient e CustomRegisteredClientRepository (implemnta RegisteredClientRepository). Por consequencia, o respetivos repository, controller e service foram implementados. Precisamos de definir no RegistedClient injetado no server definir também os topos de client AuthenticationMethod (no caso Client_Secret_Basic) e os tipos de authorizations (grant type - mais à frente). Ainda os settings do token e do client podem ser definidos.

De seguida, é necessário fazer a configuração do Authorization Server (na classe AuthorizationServerConfiguration). Na prática, vamos adicionar/criar à cadeia de filtros do spring security o authorization server. O já implementado na outra branch é o equivalente ao resource server (API). Algumas configs do token foram também definidas, como as infos adicionadas ao token (email e authorities), decoder, algoritmo usado para geração deste, criptografia etc.

Assim, foi implementado dois tipos (+1) de Authentication Flows, consoante o grant type: Authorization Code (User - Client - Resource Server) e Client Credentials (Comunicação entre API's e sistemas) (+ refresh token).

## Authorization Code
<img width="766" height="641" alt="Screenshot 2025-10-13 165531" src="https://github.com/user-attachments/assets/43bd5613-20d5-4f67-992e-db694e247bad" />

O fluxo será algo do tipo: temos um user que se quer authenticar na nossa aplicação. Acessa o sistema, que irá assim redirecionar para a Authentificação, isto é o Authorization Server. É pedido ao user para introduzir as credenciais. Se Authentication ok, é emitido um código de autentificacao (Code), que será emitido ao sistema/cliente. Neste ponto, a Authentication já está feita (já temos um objeto criado). Assim, o sistema solicita o token ao Authorization Server, passando o redirect_uri, o tipo de grant type, as credenciais e acima de tudo o Code gerado! Client assim recebe o token, e já pode realizar requisições no sistema. Uma nota importante aqui será que, da forma que a aplicação está construida, o tipo de authentication passado ao nosso sistema é uma CustomAuthentication, que implementa Authentication. No entanto, temos um JwtAuthenticationToken, que por sua vez não tem as authorities do nosso user. Então, tendo em conta que a Authentication já está realizada, adicionamos um filtro ao nosso security context (mais concretamente à cadeia de filtros do security, que sempre que ocorre Authentication via Bearer Token, a nossa implementação é executada. Essencialemnte a implementação vai pegar na authentication, buscar o user da database via repository, e setar a current authentication com a CustomAuthentication (JwtCustomAuthenticationFilter).

## Client Credentials
<img width="563" height="344" alt="Screenshot 2025-10-13 165535" src="https://github.com/user-attachments/assets/eaddff58-8606-4c3d-af25-c5d62d08e5ce" />

Este cenário é para comunicações entre sistemas/APIs. Logo, o fluxo é mais simples, visto que não há um conceito de authenticação, mas sim direcionado ao request. Portanto, o client solicita diretamente o tojen ao authorization server (passando via header o client id e secret; via body o grant type e scope). O Authprization token emite o token (com o Scope). Assim, o client pode requisitar qq recurso considerando este Scope!
