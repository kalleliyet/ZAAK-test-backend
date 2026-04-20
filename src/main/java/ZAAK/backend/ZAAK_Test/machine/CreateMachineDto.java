package ZAAK.backend.ZAAK_Test.machine;

import lombok.Data;

@Data

public class CreateMachineDto {

    private String id; // optional (you can generate it)

    private String name;
    private String location;
    private String status;

    private String model;
    private String manufacturer;
    private String description;

    private String machineTypeId;

}

