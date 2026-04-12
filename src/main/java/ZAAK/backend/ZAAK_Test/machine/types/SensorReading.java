package ZAAK.backend.ZAAK_Test.machine.types;

import lombok.Data;

@Data
public class SensorReading {
    private Double temperature;
    private Double vibration;
    private Double pressure;
    private Double current;
    private Double voltage;
}