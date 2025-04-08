package master.com.chaos_testing_framework.scheduler;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import master.com.chaos_testing_framework.service.PrometheusService;
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
    PrometheusService prometheusService;

    ConfigAndMetadataManager configAndMetadataManager;
    ExecuteService executeService;
    DockerClient dockerClient;
    @Value("${chaos_testing_framework.probability_of_rolling_back_random_container:0.25}")
    private double probabilityOfRollingBackRandomContainer;

    @Autowired
    public ConfigScheduler(ConfigAndMetadataManager configAndMetadataManager, ExecuteService executeService, DockerClient dockerClient,PrometheusService prometheusService) {
        this.configAndMetadataManager = configAndMetadataManager;
        this.executeService = executeService;
        this.dockerClient = dockerClient;
        this.prometheusService = prometheusService;
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

    @Scheduled(fixedDelayString = "1000")
    public void mayhemWatcher(){
        for (Map.Entry<String, ConfigManagerValue> element : configAndMetadataManager.getConfigs().entrySet()){
            List<MicroService> services = element.getValue().microService();
            for (MicroService microService : services) {
                String containerName = microService.getContainerName();
                InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerName).exec();
                Long currentRAM = containerResponse.getHostConfig().getMemory();
                System.out.println(currentRAM);
                prometheusService.updateMemory(containerName, currentRAM);
//                dockerClient.statsCmd(containerName).exec(new ResultCallback<Statistics>() {
//                    @Override
//                    public void onStart(Closeable closeable) {
//
//                    }
//
//                    @Override
//                    public void onNext(Statistics object) {
////                        System.out.println(object.getMemoryStats().getUsage());
//                        long memory = object.getMemoryStats().getUsage();
//                         prometheusService.updateMemory(containerName, memory);
//
//                    }
//
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void close() throws IOException {
//
//                    }
//                });
            }
        }
    }

}
