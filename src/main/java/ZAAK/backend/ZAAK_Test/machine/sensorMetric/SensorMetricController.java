package ZAAK.backend.ZAAK_Test.machine.sensorMetric;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class SensorMetricController {

    private final SensorMetricService sensorMetricService;

    @GetMapping("/machines/{machineId}/sensors/{sensorId}")
    public List<SensorMetric> getMetrics(
            @PathVariable String machineId,
            @PathVariable String sensorId) {

        return sensorMetricService.getMetrics(machineId, sensorId);
    }
}