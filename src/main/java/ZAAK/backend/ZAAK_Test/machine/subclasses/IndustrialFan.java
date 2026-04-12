package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class IndustrialFan extends Machine {

    public IndustrialFan(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange vibration = new ThresholdRange();
        vibration.setIdle(0.5);
        vibration.setNormalMax(2.0);
        vibration.setWarningMax(3.5);
        vibration.setCriticalMax(5.0);
        vibration.setSensorErrorMax(10.0);

        t.setVibration(vibration);
        return t;
    }
}