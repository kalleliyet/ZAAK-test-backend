package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.machine.sensor.SensorWithMetrics;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class MachineDetailsWithMetrics {

    private String id;
    private String name;
    private String location;
    private String status;

    private String model;
    private String manufacturer;
    private String description;

    // flatten MachineType
    private String machineTypeId;
    private String machineTypeName;
    private String machineTypeDescription;

    // sensors config
    private List<SensorWithMetrics> sensors;
}
