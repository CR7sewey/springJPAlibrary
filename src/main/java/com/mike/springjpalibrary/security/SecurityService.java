package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return userService.getByUsername(user.getUsername());
    }

}
