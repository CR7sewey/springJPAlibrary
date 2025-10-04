package com.mike.springjpalibrary.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Bean //
    public PasswordEncoder passwordEncoder() { // password wincoder interface
        return new BCryptPasswordEncoder(10);
    }

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

}
