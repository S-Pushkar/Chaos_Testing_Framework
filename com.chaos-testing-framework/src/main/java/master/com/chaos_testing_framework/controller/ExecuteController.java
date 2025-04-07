package master.com.chaos_testing_framework.controller;

import lombok.AllArgsConstructor;
import master.com.chaos_testing_framework.components.ConfigAndMetadataManager;
import master.com.chaos_testing_framework.dto.ExecuteRequest;
import master.com.chaos_testing_framework.dto.ExecuteResponse;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.ExecuteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/execute")
@AllArgsConstructor
public class ExecuteController {

    ConfigAndMetadataManager configAndMetadataManager;
    ExecuteService executeService;

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

    @PostMapping("/addConfig")
    public ResponseEntity<String> addConfig(@RequestBody ExecuteRequest request) {
        String configName = request.getConfigName();
        Status status = configAndMetadataManager.update(configName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(status.name());
    }

    @PostMapping("/removeConfig")
    public ResponseEntity<ExecuteResponse> removeConfig(@RequestBody ExecuteRequest request) {
        configAndMetadataManager.remove(request.getConfigName());
        configAndMetadataManager.rollback(request.getConfigName());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
