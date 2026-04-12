package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.machine.types.MachineStatus;
import ZAAK.backend.ZAAK_Test.machine.types.MachineThresholds;
import ZAAK.backend.ZAAK_Test.machine.types.SensorReading;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Machine {

    protected final String id;
    protected final String name;
    protected final String type;

    protected SensorReading currentReadings = new SensorReading();
    protected MachineStatus currentStatus = MachineStatus.OFFLINE;

    protected Map<String, Integer> sensorErrorCounts = new HashMap<>();

    protected static final int SENSOR_ERROR_THRESHOLD = 3;

    public Machine(MachineSpec spec) {
        this.id = spec.getId();
        this.name = spec.getName();
        this.type = spec.getType();
    }

    public abstract MachineThresholds getThresholds();

    protected MachineStatus customStatusOverride(SensorReading readings) {
        return null;
    }

    public UpdateResult updateSensorData(SensorReading readings, String explicitStatus) {

        // merge readings
        if (readings.getTemperature() != null)
            currentReadings.setTemperature(readings.getTemperature());

        if (explicitStatus != null) {
            currentStatus = MachineStatus.valueOf(explicitStatus.toUpperCase());
            return new UpdateResult(currentStatus, runDiagnostics());
        }

        List<SensorDiagnostic> diagnostics = runDiagnostics();
        currentStatus = computeStatus(diagnostics);

        return new UpdateResult(currentStatus, diagnostics);
    }

    private List<SensorDiagnostic> runDiagnostics() {
        // SAME logic as TS (simplified)
        return new ArrayList<>();
    }

    private MachineStatus computeStatus(List<SensorDiagnostic> diagnostics) {
        if (diagnostics.isEmpty()) return MachineStatus.OFFLINE;

        boolean hasCritical = diagnostics.stream().anyMatch(d -> d.getStatus().equals("critical"));
        boolean hasWarning = diagnostics.stream().anyMatch(d -> d.getStatus().equals("warning"));

        if (hasCritical) return MachineStatus.FAULT;
        if (hasWarning) return MachineStatus.WARNING;

        return MachineStatus.RUNNING;
    }
}