package ZAAK.backend.ZAAK_Test.machine.machineType;

import ZAAK.backend.ZAAK_Test.machine.machineTypeSensor.MachineTypeSensor;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "machineTypes")
public class MachineType {

    @Id
    private String id;

    private String name;
    private String description;

    private List<MachineTypeSensor> sensors;
}
