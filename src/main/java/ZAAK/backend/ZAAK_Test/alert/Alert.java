package ZAAK.backend.ZAAK_Test.alert;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String machineId;

    private String machineName;
    private String machineType;

    private String severity;
    private String message;

    private String sensor;
    private Double sensorValue;

    private Instant timestamp;

    private boolean visible;

    private List<String> resolutionNotes;
}