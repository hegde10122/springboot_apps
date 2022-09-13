package com.hegde.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testing")
    public String sayHello() {
        return "Hello Demo Security application today!!";
    }
}
