package master.com.chaos_testing_framework.service.faults;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.FaultType;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.Fault;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DecreaseRAM extends Fault {

    public DecreaseRAM(DockerClient dockerClient) {
        super(dockerClient);
    }

    @Override
    public FaultType getType() {
        return FaultType.DECREASE_RAM;
    }

    @Override
    public Status inject(String containerName) {
        try {
            InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerName).exec();
            Long currentRAM = containerResponse.getHostConfig().getMemory();

            if (currentRAM == null || currentRAM <= 0) {
                log.error("Container {} does not have any memory limit set", containerName);
                return Status.ERROR;
            }

            long newRAM = currentRAM / 10;

            dockerClient.updateContainerCmd(containerName)
                    .withMemory(newRAM)
                    .exec();

            log.info("Decreased RAM of container {} from {} to {}", containerName, currentRAM, newRAM);

            return Status.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Status.ERROR;
        }
    }
}
