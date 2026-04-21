package ZAAK.backend.ZAAK_Test.machine;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sensorMetrics")
public class SensorMetric {

    @Id
    private String id;

    private String machineId;
    private String sensorId;

    private Long bucketStart;

    private Double min;
    private Double max;
}
