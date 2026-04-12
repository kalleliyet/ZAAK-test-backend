package ZAAK.backend.ZAAK_Test.machine.types;

import lombok.Data;

@Data
public class MachineThresholds {

    private ThresholdRange temperature;
    private ThresholdRange vibration;
    private ThresholdRange pressure;
    private ThresholdRange current;
    private ThresholdRange voltage;
}