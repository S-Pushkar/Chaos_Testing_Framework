package master.com.chaos_testing_framework.model;

import lombok.Data;
import master.com.chaos_testing_framework.dto.Service;

import java.util.List;

@Data
public class Config {

    private String name;

    private List<Service> services;

}
