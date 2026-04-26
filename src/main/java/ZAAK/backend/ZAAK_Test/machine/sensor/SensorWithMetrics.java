package ZAAK.backend.ZAAK_Test.machine.sensor;

import ZAAK.backend.ZAAK_Test.machine.sensorMetric.SensorMetric;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorWithMetrics {

    private String id;
    private String name;
    private String unit;

    private List<SensorMetric> metrics; // ✅ reuse directly
}