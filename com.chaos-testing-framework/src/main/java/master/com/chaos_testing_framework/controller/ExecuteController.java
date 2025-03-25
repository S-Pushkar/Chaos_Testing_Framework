package master.com.chaos_testing_framework.controller;

import master.com.chaos_testing_framework.dto.ExecuteResponse;
import master.com.chaos_testing_framework.dto.ExecuteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/execute")
public class ExecuteController {

    @GetMapping("/hit")
    public ResponseEntity<Void> hit() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<ExecuteResponse> execute(@RequestBody ExecuteRequest request) {
        // TODO
        return null;
    }

}
