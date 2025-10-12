package com.mike.springjpalibrary.security;

import com.mike.springjpalibrary.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("ESTOU AQUI");
        // validar que é authentication oauth2, as outras ja estao customizadas
        if (authentication != null && authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            var userFound = userService.getByUsername(jwtAuthenticationToken.getName());
            if  (userFound != null) {
                CustomAuthentication auth = new CustomAuthentication(userFound);
                System.out.println("ESTOU AQUI 2");

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(request,response); // passar para o proximo filtro aka continuar requisicao

    }

}
