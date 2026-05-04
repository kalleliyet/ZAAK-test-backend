package ZAAK.backend.ZAAK_Test.machine.machineType;

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
    private Integer precision;
}