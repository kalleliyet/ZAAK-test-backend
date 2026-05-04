package ZAAK.backend.ZAAK_Test.machine.sensorMetric;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorMetricService {

    private final SensorMetricRepository repository;

    public List<SensorMetric> getMetrics(String machineId, String sensorId) {
        return repository.findByMachineIdAndSensorIdOrderByBucketStartAsc(
                machineId,
                sensorId
        );
    }
}
