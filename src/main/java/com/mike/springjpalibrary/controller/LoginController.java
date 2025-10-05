package com.mike.springjpalibrary.controller;

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
        return "Hello " + authentication.getName();
    }


}
