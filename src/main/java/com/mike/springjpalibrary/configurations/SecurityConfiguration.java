package com.mike.springjpalibrary.configurations;

import com.mike.springjpalibrary.security.CustomAuthenticationProvider;
import com.mike.springjpalibrary.security.JwtCustomAuthenticationFilter;
import com.mike.springjpalibrary.security.LoginSocialSuccessHandler;
import com.mike.springjpalibrary.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.OAuth2ResourceServerDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

//@Profile(value = {"development", "production"})
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // to make authorization in controllers
public class SecurityConfiguration { // ResourceServer - recebe o token e authentificacao

    @Bean  // http security parte do contexto do spring security; vai subscrever o security filter padrao
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler loginSocialSuccessHandler, JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {
        //OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        //        OAuth2AuthorizationServerConfigurer.authorizationServer();
        return http
                .csrf(AbstractHttpConfigurer::disable) // sem disable - protecao para fazer requisicoes pelas paginas auotrizadas (token)
                .formLogin(configurer -> configurer.loginPage("/login").permitAll()) //(Customizer.withDefaults()) // habilita via login forms; configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html")
               // .formLogin(Customizer.withDefaults())
               // .httpBasic(Customizer.withDefaults()) // habilita via http basic; https://www.debugbear.com/basic-auth-header-generator
                .authorizeHttpRequests(authorize -> {
                            authorize.requestMatchers("/login").permitAll();
                            authorize.requestMatchers(HttpMethod.POST,"/users/**").permitAll();
                           // authorize.requestMatchers(HttpMethod.POST, "/authors/**").hasRole("ADMIN"); //.hasAuthority("REGISTER_AUTHOR")
                            //authorize.requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN");
                            //authorize.requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN");
                            //authorize.requestMatchers(HttpMethod.GET, "/authors/**").hasAnyRole("ADMIN", "USER", "Admin");  // only admins can do authors/... operations
                          //  authorize.requestMatchers("/books/**").hasAnyRole("USER","ADMIN"); // everyone can access wiht roles "USER" and "ADMIN" has long as they are logged in


                            authorize.anyRequest().authenticated(); // needs to be authenticated; last rule!!

                        }
                ) // any requisition needs to be with authentication
                .oauth2Login(oauth2 -> oauth2.successHandler(
                        loginSocialSuccessHandler
                ).loginPage("/login"))
                //.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                /*.with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer.registeredClientRepository(
                        new InMemoryRegisteredClientRepository()
                ) )*/
                .oauth2ResourceServer(oauth2Resource -> {
                    oauth2Resource.jwt(Customizer.withDefaults()); // config padrao do jwt
                })
                // to execute after oauth2 login (pq o resource server é quem recebe o token e autentica o user -> assim gera o obj authentication)
                // o BearerToken... é o filtro que recebe o token e faz a verificação, isto é decodifica e faz a authentication (object) - quando token mandando na requisicao (após já ter sido amndando pelo authorization server)
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();

    }


/*
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }*/

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // salta filtro de segurança
        return web ->
            web.ignoring().requestMatchers(
                    "/v2/api-docs/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/actuator/**"
            );
    }

    @Bean // override the authtentication provider for acceptance of oauth2 (bcs the object returned is no in a UserDetails form
    public CustomAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userService, passwordEncoder);
    }

    // configura o prefixo ROLE nas authorities
    @Bean // to add the prefix when accessing the users roles; by default, uses ROLE_ (since I overwrite the Authentication Provider, this needs to be handled
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    // configura no token jwt o prefixo SCOPE
    @Bean // tal como para as autorities, ele vai vir como SCOPE_ (prefixo); entao tenho de configurar para nao ler o prefixo
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }


    /*
    @Bean                                                   //
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) { //UserDetailsService
        // In memory
        UserDetails user1 = User.builder()
                .username("user")
                .password("123")
                .passwordEncoder(pass -> passwordEncoder()
                        .encode(pass)) // spring needs to know how it will compare pass provided and in memory
                .roles("USER")
                .build();
        UserDetails user2 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();
        try {
            return new InMemoryUserDetailsManager(user1, user2);

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     */


    // OAUTHIMPL
    /*
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        List<RegisteredClient> registrations = ...
        return new InMemoryRegisteredClientRepository(registrations);
    }
    @Bean
    public RegisteredClientRepository registeredClientRepository(ClientService clientService) {
        return new CustomRegisteredClientRepository(clientService);
    }*/

}
