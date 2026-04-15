package ZAAK.backend.ZAAK_Test.machine.machineTypeSensor;

import ZAAK.backend.ZAAK_Test.machine.threshold.Threshold;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineTypeSensor {

    private String sensorId;
    private String sensorName;
    private String unit;

    private Threshold threshold;
}