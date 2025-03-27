package master.com.chaos_testing_framework.beans;

import master.com.chaos_testing_framework.service.Fault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class FaultConfig {

    @Bean
    public Map<String, Fault> faultServices(List<Fault> faults) {
        return faults.stream()
                .collect(Collectors.toMap(
                        fault -> fault.getClass().getSimpleName().toLowerCase(),
                        Function.identity()
                ));
    }
}
