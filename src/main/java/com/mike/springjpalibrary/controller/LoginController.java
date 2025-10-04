package com.mike.springjpalibrary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // web pages to make the requisition
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


}
