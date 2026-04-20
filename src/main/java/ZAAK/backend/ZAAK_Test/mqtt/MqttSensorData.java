package ZAAK.backend.ZAAK_Test.mqtt;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MqttSensorData {

    private String machineId;
    private String sensorId;

    private Double value;

    private Date timestamp; // optional
}