package ZAAK.backend.ZAAK_Test.machine;

import lombok.Data;

import java.util.List;

@Data
public class MachineRealtimePayload {

    private String id;
    private String name;
    private String type;

    private Double temperature;
    private Double vibration;
    private Double pressure;

    private String status;
    private String timestamp;

    private List<SensorDiagnostic> diagnostics;
}