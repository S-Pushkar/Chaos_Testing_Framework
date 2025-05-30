package master.com.chaos_testing_framework.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import master.com.chaos_testing_framework.dto.MicroService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data 
@Document(collection = "configs")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Config {

    @Id
    private String configName;

    private List<MicroService> services;

    private String executionType;
}
