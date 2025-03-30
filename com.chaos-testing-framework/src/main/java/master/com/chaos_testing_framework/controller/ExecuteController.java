package master.com.chaos_testing_framework.controller;

import master.com.chaos_testing_framework.dto.ExecuteResponse;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.ExecuteService;
import master.com.chaos_testing_framework.dto.ExecuteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/execute")
public class ExecuteController {

    ExecuteService executeService;
    @Autowired
    public ExecuteController(ExecuteService executeService) {
        this.executeService = executeService;
    }

    @GetMapping("/hit")
    public ResponseEntity<Void> hit() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<ExecuteResponse> execute(@RequestBody ExecuteRequest request) {
        Status executeStatus = executeService.handleExecuteRequest(request);

        if (executeStatus == Status.CONFIG_NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (executeStatus == Status.ERROR) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
