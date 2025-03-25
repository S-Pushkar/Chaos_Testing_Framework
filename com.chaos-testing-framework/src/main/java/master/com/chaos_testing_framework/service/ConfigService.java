package master.com.chaos_testing_framework.service;

import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfigService {

    private final ConfigRepository configRepository;

    @Autowired
    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Status saveConfig(Config config) {
        try {
            configRepository.save(config);
            return Status.OK;
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            return Status.ERROR;
        }
    }

}
