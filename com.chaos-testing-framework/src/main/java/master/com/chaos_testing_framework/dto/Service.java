package master.com.chaos_testing_framework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Service {

    private String containerName;

    private List<Network> networks;

    private List<Volume> volumes;

}
