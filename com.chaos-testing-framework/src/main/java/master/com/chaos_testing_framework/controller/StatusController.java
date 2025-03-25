package master.com.chaos_testing_framework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StatusController {

    @GetMapping("/health")
    public String hello() {
        return "Hello World!";
    }

}
