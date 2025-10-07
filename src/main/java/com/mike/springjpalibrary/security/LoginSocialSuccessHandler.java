package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.repository.UserRepository;
import com.mike.springjpalibrary.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // here the social login was already accepted,
        // Validate if already reg
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = auth.getPrincipal();
        var email = (String) oAuth2User.getAttribute("email");
        var userExists = userService.getByEmail(email);
        if (userExists.isPresent()) {
            CustomAuthentication authentication1 = new CustomAuthentication(userExists.get());

            SecurityContextHolder.getContext().setAuthentication(authentication1); // change context authentication to the new one
        }
        // to proceed with the requisition - redirect
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
