package master.com.chaos_testing_framework.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.ExecuteRequest;
import master.com.chaos_testing_framework.dto.FaultType;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.repository.ConfigRepository;

@Service
@Slf4j 
@AllArgsConstructor
public class ExecuteService {
    ConfigRepository configRepository;

    private final Map<FaultType, Fault> faultServices;

    // 1. Picking the faults randomly
    private FaultType randomFaultPicker() {
        List<FaultType> faultTypes = List.of(FaultType.values());
        int randomIndex = (int) (Math.random() * faultTypes.size());
        return faultTypes.get(randomIndex);
    }

    // 2. Accepting the service and the fault and executing it
    private Status executeFault(MicroService service, FaultType faultType) {
        // Here we would have the logic to execute the fault on the service
        // For now, we will just log the action
        Fault faultService = faultServices.get(faultType);
        if (faultService == null) {
            log.error("Fault service not found for type: {}", faultType);
            return Status.ERROR;
        }
        Status status = faultService.inject(service.getContainerName());
        if (status != Status.OK) {
            log.error("Failed to inject fault {} on service {}", faultType, service.getContainerName());
            return status;
        }

        log.info("Executing {} on service {}", faultType, service.getContainerName());
        return Status.OK;
    }

    private void sequentialFaultInjector(List<MicroService> services) {
        log.info("Choosing to execute SEQUENTIALLY...");
        services.stream().forEach(microService -> {
            FaultType fault = randomFaultPicker();
            Status status = executeFault(microService, fault);
            if (status == Status.OK) {
                log.info("Fault Injection for {} is {}, fault is {}", microService.getContainerName(), status.name(), fault.name());
            } else {
                log.error("Fault Injection FAILED for {}, status is {}, fault is {}", microService.getContainerName(), status.name(), fault.name());
            }
        });
    }

    private void parallelFaultInjector(List<MicroService> services) {
        log.info("Choosing to execute PARALLELLY...");
        services.parallelStream().forEach(microService -> {
            FaultType fault = randomFaultPicker();
            Status status = executeFault(microService, fault);
            if (status == Status.OK) {
                log.info("Fault Injection for {} is {}, fault is {}", microService.getContainerName(), status.name(), fault.name());
            } else {
                log.error("Fault Injection FAILED for {}, status is {}, fault is {}", microService.getContainerName(), status.name(), fault.name());
            }
        });
    }

    public Status handleExecuteRequest(ExecuteRequest executeRequest) {
        log.info("Received execute request: {}", executeRequest.getConfigName());
        Config config = configRepository.findById(executeRequest.getConfigName()).orElse(null);
        if (config == null) {
            return Status.CONFIG_NOT_FOUND;
        } 

        List<MicroService> services = config.getServices();
        String executionType = config.getExecutionType();
        if (executionType == null) {
            log.error("Execution type is not set in the config, defaulting to SEQUENTIAL");
            executionType = "sequential";
        }
        
        switch (executionType) {
            case "parallel":
                parallelFaultInjector(services);
                break;
        
            default: // sequential execution
                sequentialFaultInjector(services);
                break;
        }

        return Status.OK;
    }
}
