package master.com.chaos_testing_framework.service.faults;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import master.com.chaos_testing_framework.dto.Status;
import master.com.chaos_testing_framework.service.Fault;

public class DecreaseRAM extends Fault {

    public DecreaseRAM(DockerClient dockerClient) {
        super(dockerClient);
    }

    @Override
    public Status inject(String containerName) {
        try {
            InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerName).exec();
            Long currentRAM = containerResponse.getHostConfig().getMemory();

            if (currentRAM == null || currentRAM <= 0) {
                return Status.ERROR;
            }

            long newRAM = currentRAM / 10;

            UpdateContainerCmd updateContainerCmd = dockerClient.updateContainerCmd(containerName);
            updateContainerCmd.withMemory(newRAM);
            updateContainerCmd.exec();

            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return Status.ERROR;
        }
    }
}
