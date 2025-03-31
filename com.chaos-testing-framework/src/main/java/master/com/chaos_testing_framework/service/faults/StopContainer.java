package master.com.chaos_testing_framework.service.faults;

import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.FaultType;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.Fault;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StopContainer extends Fault {

    public StopContainer(DockerClient dockerClient) {
        super(dockerClient);
    }

    @Override
    public FaultType getType() {
        return FaultType.STOP_CONTAINER;
    }

    @Override
    public Status inject(String containerName) {
        try {
            dockerClient.stopContainerCmd(containerName).exec();
            log.info("Stopped container {}", containerName);
            return Status.OK;
        } catch (Exception e) {
            log.error("Container {} is not running", containerName);
            return Status.ERROR;
        }
    }
}
