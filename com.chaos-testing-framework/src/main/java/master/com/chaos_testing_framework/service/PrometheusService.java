package master.com.chaos_testing_framework.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PrometheusService {

    private final Map<String, ContainerMetrics> containerMetricsMap = new ConcurrentHashMap<>();

    private final MeterRegistry meterRegistry;

    @Autowired
    public PrometheusService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void updateChanges() {
        containerMetricsMap.forEach((containerName, containerMetrics) -> {
            Gauge.builder(containerName, containerMetrics.allottedMemoryMb, AtomicLong::get)
                    .tags("type", "memory")
                    .register(meterRegistry);
            Gauge.builder(containerName, containerMetrics.cpuQuota, AtomicLong::get)
                    .tags("type", "cpuQuota")
                    .register(meterRegistry);
            Gauge.builder(containerName, containerMetrics.memoryUsageMb, AtomicLong::get)
                    .tags("type", "memoryUsage")
                    .register(meterRegistry);
            Gauge.builder(containerName, containerMetrics.memoryUsagePercentage, AtomicInteger::get)
                    .tags("type", "memoryUsagePercentage")
                    .register(meterRegistry);
            Gauge.builder(containerName,containerMetrics.containerAlive,AtomicInteger::get)
                    .tags("type", "containerAlive")
                    .register(meterRegistry);
        });
    }

    public  void updateMemoryUsage(String containerName , long memory) {
        if (!containerMetricsMap.containsKey(containerName)) {
            containerMetricsMap.put(containerName, new ContainerMetrics(containerName));
        }
        containerMetricsMap.get(containerName).memoryUsageMb.set(memory);
    }

    public void updateMemoryAllocated(String containerName, long usageMb) {
        if (!containerMetricsMap.containsKey(containerName)) {
            containerMetricsMap.put(containerName, new ContainerMetrics(containerName));
        }
        containerMetricsMap.get(containerName).allottedMemoryMb.set(usageMb);
    }

    public void updateCpuQuota(String containerName, long cpuQuota) {
        containerMetricsMap.get(containerName).cpuQuota.set(cpuQuota);
    }

    public void updateMemoryUsagePercentage(String containerName, int percentage) {
        containerMetricsMap.get(containerName).memoryUsagePercentage.set(percentage);
    }

    public void setContainerAlive(String containerName, boolean alive) {
        containerMetricsMap.get(containerName).containerAlive.set(alive ? 1 : 0);
    }
    static class ContainerMetrics {
        String name;
        AtomicLong allottedMemoryMb=new AtomicLong(0);
        AtomicLong memoryUsageMb = new AtomicLong(0);
        AtomicLong cpuQuota = new AtomicLong(0);
        AtomicInteger memoryUsagePercentage = new AtomicInteger(0);
        AtomicInteger containerAlive = new AtomicInteger(1); // 1 = alive, 0 = dead

        ContainerMetrics(String name) {
            this.name = name;
        }
    }
}
