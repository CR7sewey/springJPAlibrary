package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.controller.Mappers.UserMapper;
import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.model.dto.UserDTO;
import com.mike.springjpalibrary.repository.UserRepository;
import com.mike.springjpalibrary.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String defaultPassword = "123456";

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // here the social login was already accepted,
        // Validate if already reg
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = auth.getPrincipal();
        var email = (String) oAuth2User.getAttribute("email");
        var userExists = userService.getByEmail(email);
        if (userExists.isEmpty()) {
            // autometically registered
            userExists = getUser(oAuth2User, email);

        }
        CustomAuthentication authentication1 = new CustomAuthentication(userExists.get());

        SecurityContextHolder.getContext().setAuthentication(authentication1);
        // to proceed with the requisition - redirect
        super.onAuthenticationSuccess(request, response, authentication);

    }

    private Optional<User> getUser(OAuth2User oAuth2User, String email) {
        String username = oAuth2User.getAttribute("username") != null ? oAuth2User.getAttribute("username") : ((String) oAuth2User.getAttribute("email")).substring(0, email.indexOf("@"));
        String emailOauth = oAuth2User.getAttribute("email");
        String password = oAuth2User.getAttribute("password") != null ?  oAuth2User.getAttribute("password") : defaultPassword;
        List<String> roles = List.of("USER");
        UserDTO userDTO = new UserDTO(
                username, emailOauth, password, roles
        );
        User u = userMapper.userDTOToUser(userDTO);
        userService.save(u);
        var user = userService.getByEmail(email);
        return user;
    }
}
