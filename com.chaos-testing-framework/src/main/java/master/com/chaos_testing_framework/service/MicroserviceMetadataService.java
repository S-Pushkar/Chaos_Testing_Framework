package master.com.chaos_testing_framework.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.MicroService;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.model.Config;
import master.com.chaos_testing_framework.model.MicroserviceMetadata;
import master.com.chaos_testing_framework.repository.ConfigRepository;
import master.com.chaos_testing_framework.repository.MicroserviceMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MicroserviceMetadataService {
    
    private final MicroserviceMetadataRepository microserviceMetadataRepository;
    
    private final DockerClient dockerClient;

    private final ConfigRepository configRepository;
    
    public Status saveMicroserviceMetadata(String configName, List<MicroService> services) {
        try {
            for (MicroService service : services) {
                String containerName = service.getContainerName();

                InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerName).exec();

                if (inspectContainerResponse == null) {
                    log.error("Container not found: {}", containerName);
                    return Status.ERROR;
                }

                MicroserviceMetadata microserviceMetadata = getMicroserviceMetadata(containerName, inspectContainerResponse);

                microserviceMetadata.setConfigName(configName);

                microserviceMetadataRepository.save(microserviceMetadata);
            }

            return Status.OK;
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            return Status.ERROR;
        }
    }

    private static MicroserviceMetadata getMicroserviceMetadata(String containerName, InspectContainerResponse inspectContainerResponse) {
        Long cpuQuota = inspectContainerResponse.getHostConfig().getCpuQuota();

        Long memoryLimit = inspectContainerResponse.getHostConfig().getMemory();

        List<String> networkNames = new ArrayList<>(inspectContainerResponse
                .getNetworkSettings()
                .getNetworks()
                .keySet());

        return new MicroserviceMetadata(
                containerName,
                null,
                cpuQuota,
                memoryLimit,
                networkNames
        );
    }
}
