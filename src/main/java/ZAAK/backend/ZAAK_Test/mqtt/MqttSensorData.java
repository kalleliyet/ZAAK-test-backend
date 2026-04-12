package ZAAK.backend.ZAAK_Test.mqtt;

import lombok.Data;

@Data
public class MqttSensorData {

    private String machineId;

    private String name;
    private String type;
    private String location;

    private Double temperature;
    private Double vibration;
    private Double pressure;

    private String status;

    private String lastMaintenance;
    private String nextMaintenance;
    private String installDate;

    private String model;
    private String manufacturer;
    private String description;

    private Object timestamp; // can be String or number
}