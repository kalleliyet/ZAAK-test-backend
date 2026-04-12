package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class BeltConveyor extends Machine {

    public BeltConveyor(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange vibration = new ThresholdRange();
        vibration.setIdle(0.3);
        vibration.setNormalMax(1.5);
        vibration.setWarningMax(2.5);
        vibration.setCriticalMax(4.0);
        vibration.setSensorErrorMax(8.0);

        t.setVibration(vibration);

        return t;
    }
}