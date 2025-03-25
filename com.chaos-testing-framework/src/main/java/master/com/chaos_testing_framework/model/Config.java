package master.com.chaos_testing_framework.model;

import lombok.Data;
import master.com.chaos_testing_framework.dto.Service;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collation = "configs")
public class Config {

    @Id
    private String name;

    private List<Service> services;

}
