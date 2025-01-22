package com.master.chaos_agent.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Hello {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
