package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class HydraulicPump extends Machine {

    public HydraulicPump(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange pressure = new ThresholdRange();
        pressure.setIdle(10);
        pressure.setNormalMax(100);
        pressure.setWarningMax(120);
        pressure.setCriticalMax(150);
        pressure.setSensorErrorMax(300);

        t.setPressure(pressure);
        return t;
    }
}