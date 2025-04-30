package master.com.chaos_testing_framework.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.model.MicroserviceMetadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class RollbackContainerService {

    private DockerClient dockerClient;

    public void rollbackContainer(MicroserviceMetadata microserviceMetadata) {
        String containerName = microserviceMetadata.getContainerName();

        try {

            Long cpuQuota = Long.parseLong(String.valueOf(microserviceMetadata.getCpuQuota()));

            Long memoryLimit = microserviceMetadata.getMemoryLimit();

            List<String> networkNames = microserviceMetadata.getNetworkNames();

            InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerName).exec();

            Set<String> connectedNetworks = inspectContainerResponse.getNetworkSettings().getNetworks().keySet();

            dockerClient.updateContainerCmd(containerName)
                    .withCpuQuota(cpuQuota)
                    .withMemory(memoryLimit)
                    .exec();

            for (String networkName : networkNames) {
                if (connectedNetworks.contains(networkName)) {
                    continue;
                }

                dockerClient.connectToNetworkCmd()
                        .withContainerId(containerName)
                        .withNetworkId(networkName)
                        .exec();
            }

            dockerClient.restartContainerCmd(containerName).exec();

            log.info("Successfully rolled back container: {}", containerName);
        } catch (Exception e) {
            log.error("Failed to rollback container {}: {}", containerName, e.getMessage());
        }
    }
}
