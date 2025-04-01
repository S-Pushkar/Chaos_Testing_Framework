package master.com.chaos_testing_framework.dto;

import java.util.List;

public record ConfigManagerValue(List<MicroService> microService, long timestamp) {
	
}
