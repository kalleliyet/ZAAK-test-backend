package ZAAK.backend.ZAAK_Test.machine.types;

import lombok.Data;

@Data
public class ThresholdRange {
    private double idle;
    private double normalMax;
    private double warningMax;
    private double criticalMax;
    private double sensorErrorMax;
    private Double abnormalLow;
}