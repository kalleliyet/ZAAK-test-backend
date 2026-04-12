package ZAAK.backend.ZAAK_Test.machine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorDiagnostic {

    private String sensor;   // temperature, vibration...
    private double value;
    private String status;   // normal | warning | critical | sensor_error
    private String message;
}