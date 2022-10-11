package com.hegde.springbootlearning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//This is a REST API controller.
@RestController
public class HelloController {

    //GET request when this endpoint is HIT
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String welcome(){
        return "Welcome Happy Diwali";
    }

    //GET request shortcut
    @GetMapping("/quarantine")
    public String quarantine(){
        return "Have you taken the booster dose ?";
    }

    //GET request
    @GetMapping("/hello3")
    public String hello(){
        return "Hello Friend, did you find the joke amusing ?";
    }
}
