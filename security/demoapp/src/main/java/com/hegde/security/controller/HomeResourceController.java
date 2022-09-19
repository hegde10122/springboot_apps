package com.hegde.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResourceController {

    @GetMapping("/home")
    public String getHome() {
        return "<h1>Welcome son !!</h1>";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "<h1>Welcome admin !!</h1>";
    }

    @GetMapping("/user")
    public String getUser() {
        return "<h1>Welcome user !!</h1>";
    }
}