# Authorization Server

- Link: https://docs.spring.io/spring-authorization-server/reference/core-model-components.html

Como está na previous branch, temos a authentificação via Basic Http (username e password) e Login Social (que usa a api OAuth2 da google).

O objetivo aqui foi implementar algo semelhante, isto é, o protocolo OAuth2. O flow básico de authentificação seria: User acessa o sistema (client), é redirecionado para o authorization server, solicita a authentificação e faz o login. O authorization server emite um token ao client, permitindo assim o acesso à API (Resource Server).

<img width="794" height="512" alt="Screenshot 2025-10-13 165106" src="https://github.com/user-attachments/assets/c1557480-e856-4694-8acb-36977082cae8" />
<img width="562" height="682" alt="Screenshot 2025-10-13 165151" src="https://github.com/user-attachments/assets/f24f76d6-ed2e-4fc4-9104-d1e0cec1f823" />

Assim, foi implementado dois tipos de Authentication Flows, consoante o grant type: Authorization Code (User - Client - Resource Server) e Client Credentials (Comunicação entre API's e sistemas).

## Authorization Code
<img width="766" height="641" alt="Screenshot 2025-10-13 165531" src="https://github.com/user-attachments/assets/43bd5613-20d5-4f67-992e-db694e247bad" />

Essencialmente, para fazer a authentificação, temos de definir um RegisteredClient. Este é o equivalente a um UserDetails, isto é, a partir, que será injetado no contexto do spring security, poderemos aceder e formar o server. Parametros principais são clientId (id), clientSecret (pass), redirectUri (para onde será enviado o code no step 1), scope (escopo de atuação - aka authorities).

## Client Credentials
<img width="563" height="344" alt="Screenshot 2025-10-13 165535" src="https://github.com/user-attachments/assets/eaddff58-8606-4c3d-af25-c5d62d08e5ce" />


RegisteredClientRepostiory - similar to UserDetailsService
