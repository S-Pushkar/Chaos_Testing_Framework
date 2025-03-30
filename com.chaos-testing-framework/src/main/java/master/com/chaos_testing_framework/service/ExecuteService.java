package master.com.chaos_testing_framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.ExecuteRequest;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.repository.ConfigRepository;

@Service
@Slf4j 
public class ExecuteService {
    ConfigRepository configRepository;

    @Autowired
    public ExecuteService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Status handleExecuteRequest(ExecuteRequest executeRequest) {
        log.info("Received execute request: {}", executeRequest.getConfigName());
        Config config = configRepository.findById(executeRequest.getConfigName()).orElse(null);
        if (config == null) {
            return Status.CONFIG_NOT_FOUND;
        } 

        List<MicroService> services = config.getServices();
        log.info(services.toString());
        return Status.OK;
    }
}
