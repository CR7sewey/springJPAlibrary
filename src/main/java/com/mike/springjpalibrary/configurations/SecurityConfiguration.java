package com.mike.springjpalibrary.configurations;

import com.mike.springjpalibrary.authorizationServer.CustomRegisteredClientRepository;
import com.mike.springjpalibrary.security.CustomAuthenticationProvider;
import com.mike.springjpalibrary.security.CustomUserDetailsService;
import com.mike.springjpalibrary.security.LoginSocialSuccessHandler;
import com.mike.springjpalibrary.service.ClientService;
import com.mike.springjpalibrary.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // to make authorization in controllers
public class SecurityConfiguration {

    @Bean  // http security parte do contexto do spring security; vai subscrever o security filter padrao
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler loginSocialSuccessHandler) throws Exception {
        //OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        //        OAuth2AuthorizationServerConfigurer.authorizationServer();
        return http
                .csrf(AbstractHttpConfigurer::disable) // sem disable - protecao para fazer requisicoes pelas paginas auotrizadas (token)
                .formLogin(configurer -> configurer.loginPage("/login").permitAll()) //(Customizer.withDefaults()) // habilita via login forms; configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html")
               // .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults()) // habilita via http basic; https://www.debugbear.com/basic-auth-header-generator
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
                .build();

    }

    @Bean //
    public PasswordEncoder passwordEncoder() { // password wincoder interface
        return new BCryptPasswordEncoder(10);
    }
/*
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }*/

    @Bean // override the authtentication provider for acceptance of oauth2 (bcs the object returned is no in a UserDetails form
    public CustomAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userService, passwordEncoder);
    }

    @Bean // to add the prefix when accessing the users roles; by default, uses ROLE_ (since I overwrite the Authentication Provider, this needs to be handled
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
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
    } */
    @Bean
    public RegisteredClientRepository registeredClientRepository(ClientService clientService) {
        return new CustomRegisteredClientRepository(clientService);
    }

}
