package ZAAK.backend.ZAAK_Test.machine;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "machines")
public class Machine {

    @Id
    private String id;

    private String name;
    private String location;
    private String status;

    private LocalDate installDate;
    private LocalDate lastMaintenance;
    private LocalDate nextMaintenance;

    private String model;
    private String manufacturer;
    private String description;

    // Reference to MachineType
    private String machineTypeId;
}