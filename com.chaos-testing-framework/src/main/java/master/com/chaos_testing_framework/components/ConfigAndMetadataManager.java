package master.com.chaos_testing_framework.components;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.model.MicroserviceMetadata;
import master.com.chaos_testing_framework.repository.MicroserviceMetadataRepository;
import master.com.chaos_testing_framework.service.MicroserviceMetadataService;
import master.com.chaos_testing_framework.service.RollbackContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import master.com.chaos_testing_framework.dto.ConfigManagerValue;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.repository.ConfigRepository;

@Getter
@Component
@Slf4j
public class ConfigAndMetadataManager {
    private final Map<String, ConfigManagerValue> configs;
    private final ConfigRepository configRepository;
    private final MicroserviceMetadataService microserviceMetadataService;
    private final RollbackContainerService rollbackContainerService;
    private final MicroserviceMetadataRepository microserviceMetadataRepository;

    @Autowired
    public ConfigAndMetadataManager(ConfigRepository configRepository, MicroserviceMetadataService microserviceMetadataService, RollbackContainerService rollbackContainerService, MicroserviceMetadataRepository microserviceMetadataRepository) {
        this.microserviceMetadataService = microserviceMetadataService;
        this.rollbackContainerService = rollbackContainerService;
        this.microserviceMetadataRepository = microserviceMetadataRepository;
        this.configs = new HashMap<>();
        this.configRepository = configRepository;
    }

    public Status update(String configName) {
        Config config = configRepository.findById(configName).orElse(null);
        updateConfigsForChaosTest(config);
        return updateMetadata(configName, config.getServices());
    }

    public void rollback(String configName) {
        microserviceMetadataRepository.findByConfigName(configName).forEach(rollbackContainerService::rollbackContainer);
    }

    public void rollbackRandomContainer(String configName) {
        List<MicroService> services = configs.get(configName).microService();

        int randomIndex = (int) (Math.random() * services.size());

        MicroService randomService = services.get(randomIndex);

        MicroserviceMetadata microserviceMetadata = microserviceMetadataRepository
                .findById(randomService.getContainerName())
                .orElse(null);
        if (microserviceMetadata == null) {
            log.error("Microservice " + randomService.getContainerName() + " not found");
            return;
        }

        rollbackContainerService.rollbackContainer(microserviceMetadata);

        log.info("Rolled back container {}", microserviceMetadata.getContainerName());
    }

    private Status updateMetadata(String configName, List<MicroService> services) {
        return microserviceMetadataService.saveMicroserviceMetadata(configName, services);
    }

    private void updateConfigsForChaosTest(Config config) {
        long timestamp = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))
                                        .toInstant()
                                        .toEpochMilli();
        configs.put(config.getConfigName(), new ConfigManagerValue(config.getServices(), timestamp));
    }

    public void updateConfigsForChaosTest(String configName, List<MicroService> services, Long timestamp) {
        configs.put(configName, new ConfigManagerValue(services, timestamp));
    }

    public void remove(String configName) {
        configs.remove(configName);
    }

}
