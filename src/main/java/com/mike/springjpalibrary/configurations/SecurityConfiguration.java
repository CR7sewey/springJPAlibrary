package com.mike.springjpalibrary.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean  // http security parte do contexto do spring security; vai subscrever o security filter padrao
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable) // sem disable - protecao para fazer requisicoes pelas paginas auotrizadas (token)
                .formLogin(configurer -> configurer.loginPage("/login").permitAll()) //(Customizer.withDefaults()) // habilita via login forms; configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html")
                .httpBasic(Customizer.withDefaults()) // habilita via http basic; https://www.debugbear.com/basic-auth-header-generator
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()) // any requisition needs to be with authentication
                .build();

    }

}
