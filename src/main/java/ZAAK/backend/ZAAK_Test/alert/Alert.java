package ZAAK.backend.ZAAK_Test.alert;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String machineId;
    private String machineName;
    private String machineType;

    private String category;

    private String severity; // "info" | "warning" | "critical"

    private String message;

    private String sensor;
    private Double sensorValue;

    private Date timestamp = new Date();

    private Boolean visible = true;

    private List<String> resolutionNotes;
}