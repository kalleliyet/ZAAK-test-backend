package ZAAK.backend.ZAAK_Test.machine;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class SensorMetricController {

    private final SensorMetricRepository repository;

    // 🔥 Get metrics for a sensor (for chart)
    @GetMapping("/machines/{machineId}/sensors/{sensorId}")
    public List<SensorMetric> getMetrics(
            @PathVariable String machineId,
            @PathVariable String sensorId) {

        return repository.findByMachineIdAndSensorIdOrderByBucketStartAsc(machineId, sensorId);
    }
}