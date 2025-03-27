package master.com.chaos_testing_framework.controller;

import lombok.RequiredArgsConstructor;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.Fault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fault-test/")
@RequiredArgsConstructor
public class FaultTestController {

    private final Map<String, Fault> faultServices;

    @PostMapping("/{faultType}")
    public ResponseEntity<String> injectFault(
            @PathVariable String faultType,
            @RequestParam String containerName) {

        Fault faultService = faultServices.get(faultType);
        if (faultService == null) {
            return ResponseEntity.badRequest().body("Invalid fault type");
        }

        Status status = faultService.inject(containerName);
        return status == Status.OK ?
                ResponseEntity.ok(faultType + " injected successfully") :
                ResponseEntity.badRequest().body("Failed to inject " + faultType);
    }
}
