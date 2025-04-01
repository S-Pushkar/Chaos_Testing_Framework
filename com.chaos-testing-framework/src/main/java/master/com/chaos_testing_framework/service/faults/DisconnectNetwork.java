package master.com.chaos_testing_framework.service.faults;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import lombok.extern.slf4j.Slf4j;
import master.com.chaos_testing_framework.dto.FaultType;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.Fault;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class DisconnectNetwork extends Fault {

    public DisconnectNetwork(DockerClient dockerClient) {
        super(dockerClient);
    }

    @Override
    public FaultType getType() {
        return FaultType.DISCONNECT_NETWORK;
    }

    @Override
    public Status inject(String containerName) {
        try {
            InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerName).exec();
            Map<String, ContainerNetwork> networks = containerResponse.getNetworkSettings().getNetworks();

            if (networks.isEmpty()) {
                log.error("Container {} is not connected to any network", containerName);
                return Status.ERROR;
            }

            List<String> networkNames = new ArrayList<>(networks.keySet());

            String randomNetworkName = networkNames.get(new Random().nextInt(networkNames.size()));

            dockerClient.disconnectFromNetworkCmd()
                    .withContainerId(containerName)
                    .withNetworkId(randomNetworkName)
                    .withForce(true)
                    .exec();

            log.info("Disconnected network {} from container {}", randomNetworkName, containerName);

            return Status.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Status.ERROR;
        }
    }
}
