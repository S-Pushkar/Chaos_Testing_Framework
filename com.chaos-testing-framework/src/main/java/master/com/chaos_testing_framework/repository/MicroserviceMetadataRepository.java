package master.com.chaos_testing_framework.repository;

import master.com.chaos_testing_framework.model.MicroserviceMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MicroserviceMetadataRepository extends MongoRepository<MicroserviceMetadata, String> {
    List<MicroserviceMetadata> findByConfigName(String configName);
}
