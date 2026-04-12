package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class Generator extends Machine {

    public Generator(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange voltage = new ThresholdRange();
        voltage.setIdle(210);
        voltage.setNormalMax(240);
        voltage.setWarningMax(260);
        voltage.setCriticalMax(300);
        voltage.setSensorErrorMax(500);

        t.setVoltage(voltage);

        return t;
    }
}