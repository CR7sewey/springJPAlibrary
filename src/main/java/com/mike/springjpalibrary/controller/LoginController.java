package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // web pages to make the requisition
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String homePage(Authentication authentication) {
        if (authentication instanceof CustomAuthentication customAuthentication)
            System.out.println(customAuthentication);
        return "Hello " + authentication.getName();
    }


}
