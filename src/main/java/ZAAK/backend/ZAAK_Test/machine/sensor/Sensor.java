package ZAAK.backend.ZAAK_Test.machine.sensor;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sensors")
public class Sensor {

    @Id
    private String id;

    private String name;
    private String unit;
}
