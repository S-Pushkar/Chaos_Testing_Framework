package master.com.chaos_testing_framework.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MicroService {

    private String containerName;

    private List<String> networks;

    private List<Volume> volumes;

}
