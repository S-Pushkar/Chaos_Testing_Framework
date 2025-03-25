package master.com.chaos_testing_framework.repository;

import master.com.chaos_testing_framework.model.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<Config, String> {

}
