package ZAAK.backend.ZAAK_Test.alert;

import lombok.Data;
import java.util.List;

@Data
public class CreateAlertDto {

    private String machineId;
    private String machineName;
    private String machineType;

    private String category;
    private String severity;

    private String message;

    private String sensor;
    private Double sensorValue;

    private Boolean visible;
    private List<String> resolutionNotes;
}