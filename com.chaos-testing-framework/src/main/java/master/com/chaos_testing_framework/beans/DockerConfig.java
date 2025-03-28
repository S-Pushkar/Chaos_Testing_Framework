package master.com.chaos_testing_framework.beans;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Docker client
 * NOTE: To make this work, you'll need to change /lib/systemd/system/docker.service file such that
 * ExecStart=/usr/bin/dockerd -H fd:// -H tcp://127.0.0.1:2375 --containerd=/run/containerd/containerd.sock
 * This will allow the Docker client to connect to the Docker daemon via TCP
 */
@Configuration
public class DockerConfig {

    private static final Logger logger = LoggerFactory.getLogger(DockerConfig.class);

    @Bean
    public DockerClient dockerClient() {
        return DockerClientImpl.getInstance(
                DefaultDockerClientConfig.createDefaultConfigBuilder().build(),
                new ApacheDockerHttpClient.Builder()
                        .dockerHost(DefaultDockerClientConfig.createDefaultConfigBuilder().build().getDockerHost())
                        .build()
        );
    }
}