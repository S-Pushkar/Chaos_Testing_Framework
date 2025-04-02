package master.com.chaos_testing_framework.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "microservice_metadata")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class MicroserviceMetadata {
    @Id
    private String containerName;

    private String configName;

    private Long cpuQuota;

    private Long memoryLimit;

    private List<String> networkNames;


}
