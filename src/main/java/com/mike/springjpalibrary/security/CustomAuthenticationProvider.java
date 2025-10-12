package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor // override authentication provider
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString(); // could be digital, facial password, not only digited!
        System.out.println(username + " is authenticated " + password);
        //String encodedPassword = passwordEncoder.encode(password);
        System.out.println(authentication); // null in the first part (before token)
        // validate if username + password check in database
        var userFound = userService.getByUsername(username);
        System.out.println(passwordEncoder.matches(password,userFound.getPassword()));
        if (userFound != null && passwordEncoder.matches( password,userFound.getPassword())) {
            System.out.println("AQUI ZE");
            CustomAuthentication auth = new CustomAuthentication(userFound);
            return auth;
        } else if ( userFound == null) {
            throw new AuthenticationCredentialsNotFoundException("User not found");
        }
        throw new AuthenticationCredentialsNotFoundException("Password invalid!");


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
