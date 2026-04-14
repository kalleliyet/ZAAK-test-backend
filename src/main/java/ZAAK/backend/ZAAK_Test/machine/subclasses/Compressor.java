package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class Compressor extends Machine {

    public Compressor(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange temp = new ThresholdRange();
        temp.setIdle(25);
        temp.setNormalMax(70);
        temp.setWarningMax(80);
        temp.setCriticalMax(95);
        temp.setSensorErrorMax(200);

        t.setTemperature(temp);
        return t;
    }
}