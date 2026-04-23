package ZAAK.backend.ZAAK_Test.machine;

import lombok.Data;

import java.time.LocalDate;

@Data

public class CreateMachineDto {

    private String id; // optional (you can generate it)

    private String name;
    private String serialNumber;
    private String location;
    private String status;

    private LocalDate installDate;
    private LocalDate lastMaintenance;
    private LocalDate nextMaintenance;

    private String model;
    private String manufacturer;
    private String description;

    private String machineTypeId;

}

