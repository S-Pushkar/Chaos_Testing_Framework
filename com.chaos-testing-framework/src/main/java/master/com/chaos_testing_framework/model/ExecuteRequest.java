package master.com.chaos_testing_framework.model;

import lombok.Data;

import java.util.Date;

@Data
public class ExecuteRequest {

    String configName;

    Date executeAt;

}
