package master.com.chaos_testing_framework.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrometheusService {

    private final MeterRegistry meterRegistry;

    @Autowired
    public PrometheusService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementCounter(String counterName) {
        meterRegistry.counter(counterName).increment();
    }

    public void updateMemory (String counterName , long memory) {
//        meterRegistry.counter(counterName, "metricType", "memory").increment(memory);

//        Counter counter = Counter.builder(counterName)
//                .tag("metricType", "memory")
//                .register(meterRegistry);
//
//        counter.increment(memory);

        meterRegistry.gauge(counterName, memory);
    }
}
