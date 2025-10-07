## Diagrams

### Logic

The spring security é como um envelope que engloba a minha aplicação num contexto especifico,
e que sendo especificada, sobreescreve parte dos metodos desta (SecurityConfiguration).

Podemos dividir isto em 3 partes:
- UserDetailsService: implementa uma interface que vai buscar as infos do user ao repository e devolve um objeto do tipo UserDetails
- UserDetails: pbjeto que contem o username, password, roles etc,e que fornece posteriormente uma authentication sobre o qual o spring security vai implementar o seu contexto
- Authentication: objeto com uma serie de configs para ser aplicadas no contexto da security

### Basic Authentication

Quando se trabalha com a basic authentication, é relativamente simples. Basta fazer uma implementação da UserDetailsService,
feita aqui pela classe CustomUserDetailsService, onde se vai fazer a verificação se existe na db. Assim, cria-se o objeto
UserDetails, que por sua vez irá criar (default) o objeto authentication para o contexto do security (passado atraves do userDetailsService no SecurtiyConfiguration)

### Basic Authentication e OAuth2 via web (form login e oauth)

Neste cenário em especifico, convém criar a nossa propria authentication (CustomAuthentication). Essencialmente, espa implementação
é uma implementação de authenticaiton, que é o que eu vou passar eventualmente ao meu contexto do spring security. No entanto,
não so assim temos mais controlo sobre a propria authentication, o que passamos e etc, como será mais facil manipular diferentes
authentication objects que vem, pois qunaod usamos a api do google para fazer o login o objeto authentication retornado é
diferente do Authentication padrao.

Portanto, para isto, temos o CustomAuthentication, que nada mais é que uma implementação de Authentication, e um CustomAuthenticationProvider.
Como o nome indica, o provider é que vai providenciar ao contexto do security o object CustomAuthentication. Ele implementa assim o AuthenticationProvider,
nomeadamente o metodo authentication. Este metodo retorna uma authentication (no caso a customizada), e também neste metodo
fazemos a validacao de que a password fornecida faz match com a do user. Ou seja, na pratica embora a authentication até ja esteja feita,
pois ja temos o objeto Authentication, só depois é que de facto é considerado em todo o contexto do security, daí essa validação neste passo.

A "arvore de dependecias" aqui seria algo do tipo:

CustomAuthenticationProvider -- implementa --> AuthenticationProvider -- implementa --> metodo de authentication que retorna objeto do tipo Authentication
CustomAuthentication -- implementa --> Authentication (depende do UserDetails)
Tudo isto passado no SecurityConfiguration.

No entanto, as it stands, ha um problema: embora ele permita o login via oauth (social), no sentido em que não dá um erro,
aquilo não representa nada no meu sistema. Isto é, primeiro o tipo de authentication object retornado é diferente (embora implemente Authentication);
segundo, eu aquele login, o user, não esta a ser validado na minha base de dados, os roles e se existe. Logo, na pratica auele user
que está a formar o authentication é lixo.

A solução é a seguinte: criar uma implementação de SavedRequestAwareAuthenticationSuccessHandler, que não vai em sim fazer a authentificacao,
pois isso é lidado pela API do google, mas sim em caso de sucesso, pegar na authentication retornada, pegar num parametro, no caso o email, 
e ir buscar o meu user, e assim retornar a minha authentication customizada. Para isos, tmb no SecurityFilterChain das configuracoes do Security,
tenho de passar esta impl. Feito em LoginSocialSuccessHandler!

<img width="615" height="407" alt="Screenshot 2025-10-07 155201" src="https://github.com/user-attachments/assets/d1a08b23-e0fe-4678-8f63-f3853032582a" />

