package ZAAK.backend.ZAAK_Test.machine.subclasses;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.ThresholdRange;

public class ElectricMotor extends Machine {

    public ElectricMotor(MachineSpec spec) {
        super(spec);
    }

    @Override
    public MachineThresholds getThresholds() {
        MachineThresholds t = new MachineThresholds();

        ThresholdRange current = new ThresholdRange();
        current.setIdle(2);
        current.setNormalMax(10);
        current.setWarningMax(15);
        current.setCriticalMax(20);
        current.setSensorErrorMax(50);

        t.setCurrent(current);

        ThresholdRange temp = new ThresholdRange();
        temp.setIdle(30);
        temp.setNormalMax(80);
        temp.setWarningMax(100);
        temp.setCriticalMax(120);
        temp.setSensorErrorMax(200);

        t.setTemperature(temp);

        return t;
    }
}