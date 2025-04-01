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
public class DecreaseCPUs extends Fault {

    public DecreaseCPUs(DockerClient dockerClient) {
        super(dockerClient);
    }

    @Override
    public FaultType getType() {
        return FaultType.DECREASE_CPUS;
    }

    @Override
    public Status inject(String containerName) {
        try {
            InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerName).exec();

            Long cpuQuota = containerResponse.getHostConfig().getCpuQuota();

            if (cpuQuota == null || cpuQuota <= 0) {
                log.error("Container {} does not have any CPU limit set", containerName);
                return Status.ERROR;
            }

            int newCpuQuota = (int) (cpuQuota / 10);

            dockerClient.updateContainerCmd(containerName)
                    .withCpuQuota(newCpuQuota)
                    .exec();

            log.info("Decreased CPU quota of container {} from {} to {}", containerName, cpuQuota, newCpuQuota);

            return Status.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Status.ERROR;
        }
    }
}
