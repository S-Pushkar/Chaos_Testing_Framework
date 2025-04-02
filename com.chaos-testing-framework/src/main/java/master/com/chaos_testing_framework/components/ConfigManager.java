package master.com.chaos_testing_framework.components;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import master.com.chaos_testing_framework.dto.ConfigManagerValue;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.repository.ConfigRepository;

@Getter
@AllArgsConstructor
@Component
public class ConfigManager {
    Map<String, ConfigManagerValue> configs;
    ConfigRepository configRepository;

    public void update(String configName) {
        Config config = configRepository.findById(configName).orElse(null);
        long timestamp = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))
                                        .toInstant()
                                        .toEpochMilli();
        configs.put(config.getConfigName(), new ConfigManagerValue(config.getServices(), timestamp));
    }

    public void update(String configName, List<MicroService> services, Long timestamp) {
        configs.put(configName, new ConfigManagerValue(services, timestamp));
    }

}
