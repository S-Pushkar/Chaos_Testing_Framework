package master.com.chaos_testing_framework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Hello {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
