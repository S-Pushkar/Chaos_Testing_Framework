package master.com.chaos_testing_framework.scheduler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.components.ConfigAndMetadataManager;
import master.com.chaos_testing_framework.dto.ConfigManagerValue;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.service.ExecuteService;

@EnableScheduling
@Slf4j
@Service
public class ConfigScheduler {
    ConfigAndMetadataManager configAndMetadataManager;
    ExecuteService executeService;

    @Value("${chaos_testing_framework.probability_of_rolling_back_random_container:0.25}")
    private double probabilityOfRollingBackRandomContainer;

    @Autowired
    public ConfigScheduler(ConfigAndMetadataManager configAndMetadataManager, ExecuteService executeService) {
        this.configAndMetadataManager = configAndMetadataManager;
        this.executeService = executeService;
    }

    @Scheduled(fixedDelayString = "2500")
    public void mayhemScheduler() {
        for (Map.Entry<String, ConfigManagerValue> element : configAndMetadataManager.getConfigs().entrySet()) {
            String configName = element.getKey();
            List<MicroService> services = element.getValue().microService();
            Long timestamp = element.getValue().timestamp();

            long currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))
                                        .toInstant()
                                        .toEpochMilli();

            if(currentTime < timestamp) {
                continue;
            }

            double randomNumber = Math.random();
            if (randomNumber <= probabilityOfRollingBackRandomContainer) {
                configAndMetadataManager.rollbackRandomContainer(configName);
                continue;
            }

            executeService.singletonFaultInjector(services);
            currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))
                                        .toInstant()
                                        .toEpochMilli();

            long randomSeconds = (long) (5 + Math.random() * (30 - 5)) * 1000L;

            // Add random seconds to timestamp
            long newTimeStamp = currentTime + randomSeconds;
            configAndMetadataManager.updateConfigsForChaosTest(element.getKey(), services, newTimeStamp);
        }
    }
}
