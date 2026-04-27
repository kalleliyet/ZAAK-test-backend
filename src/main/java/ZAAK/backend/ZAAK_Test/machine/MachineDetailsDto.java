package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.machine.machineType.MachineTypeSensor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class MachineDetailsDto {

    private String id;
    private String name;
    private String serialNumber;
    private String location;
    private String status;

    private String model;
    private String manufacturer;
    private String description;

    private LocalDate installDate;
    private LocalDate lastMaintenance;
    private LocalDate nextMaintenance;

    // flatten MachineType
    private String machineTypeId;
    private String machineTypeName;
    private String machineTypeDescription;

    // sensors config
    private List<MachineTypeSensor> sensors;
}
