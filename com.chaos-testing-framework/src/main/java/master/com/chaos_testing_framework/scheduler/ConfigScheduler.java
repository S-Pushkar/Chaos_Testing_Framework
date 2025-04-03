package master.com.chaos_testing_framework.scheduler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.components.ConfigAndMetadataManager;
import master.com.chaos_testing_framework.dto.ConfigManagerValue;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.service.ExecuteService;

@EnableScheduling
@AllArgsConstructor
@Slf4j
@Service
public class ConfigScheduler {
    ConfigAndMetadataManager configAndMetadataManager;
    ExecuteService executeService;

    @Scheduled(fixedDelayString = "2500")
    public void mayhemScheduler() {
        for (Map.Entry<String, ConfigManagerValue> element : configAndMetadataManager.getConfigs().entrySet()) {
            List<MicroService> services = element.getValue().microService();
            Long timestamp = element.getValue().timestamp();

            long currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))
                                        .toInstant()
                                        .toEpochMilli();

            if(currentTime < timestamp) {
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
