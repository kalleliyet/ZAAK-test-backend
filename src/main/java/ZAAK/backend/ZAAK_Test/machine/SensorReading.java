package ZAAK.backend.ZAAK_Test.machine;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading {

    private String machineId;
    private String sensorId;
    private Double value;
    private Instant timestamp;
}
