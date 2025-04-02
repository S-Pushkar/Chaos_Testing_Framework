package master.com.chaos_testing_framework.components;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import master.com.chaos_testing_framework.model.MicroserviceMetadata;
import master.com.chaos_testing_framework.repository.MicroserviceMetadataRepository;
import master.com.chaos_testing_framework.service.RollbackContainerService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ShutDownHook {

    private final MicroserviceMetadataRepository microserviceMetadataRepository;

    private final RollbackContainerService rollbackContainerService;

    @PreDestroy
    public void onExit() {
        List<MicroserviceMetadata> metadataList = microserviceMetadataRepository.findAll();

        for (MicroserviceMetadata metadata : metadataList) {
            rollbackContainerService.rollbackContainer(metadata);
        }

        microserviceMetadataRepository.deleteAll();
    }
}
