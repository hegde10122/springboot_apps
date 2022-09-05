package com.example.springboot.learner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @RequestMapping(value = "/basics", method = RequestMethod.GET)
    public String basicApp() {
        return "I am enjoying time with endpoints!!";
    }

    // for requestmethod.get, we can do this...to remove verbosity---getMapping for
    // GET, DeleteMapping for DELETE etc
    @GetMapping("/basics2")
    public String basicApp2() {
        return "I am sleeping with this!!";
    }

}
