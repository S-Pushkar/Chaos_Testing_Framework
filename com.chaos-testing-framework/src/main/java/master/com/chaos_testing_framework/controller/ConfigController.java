package master.com.chaos_testing_framework.controller;

import master.com.chaos_testing_framework.model.Config;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/config/")
public class ConfigController {

    @GetMapping("/hit")
    public ResponseEntity<Void> hit() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody Config config) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
