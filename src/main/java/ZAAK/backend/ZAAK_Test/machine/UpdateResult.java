package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.machine.types.MachineStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateResult {
    private MachineStatus status;
    private List<SensorDiagnostic> diagnostics;
}