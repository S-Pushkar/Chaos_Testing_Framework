package master.com.chaos_testing_framework.service;

import com.github.dockerjava.api.DockerClient;
import lombok.RequiredArgsConstructor;
import master.com.chaos_testing_framework.dto.FaultType;
import master.com.chaos_testing_framework.dto.Status;

@RequiredArgsConstructor
public abstract class Fault {

    public final DockerClient dockerClient;

    public abstract FaultType getType();

    public abstract Status inject(String containerName);
}
