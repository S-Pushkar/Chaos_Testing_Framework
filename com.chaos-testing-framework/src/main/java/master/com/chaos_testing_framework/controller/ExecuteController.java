package master.com.chaos_testing_framework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/execute")
public class ExecuteController {

    @GetMapping("/hit")
    public ResponseEntity<Void> hit() {
        return ResponseEntity.noContent().build();
    }

}
