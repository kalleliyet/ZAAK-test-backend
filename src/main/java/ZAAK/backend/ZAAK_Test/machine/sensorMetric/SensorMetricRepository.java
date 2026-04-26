package ZAAK.backend.ZAAK_Test.machine.sensorMetric;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SensorMetricRepository extends MongoRepository<SensorMetric, String> {
    List<SensorMetric> findByMachineIdAndSensorIdOrderByBucketStartAsc(String machineId, String sensorId);
    List<SensorMetric> findByMachineIdOrderByBucketStartAsc(String machineId);
}
