package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.Redis.RedisSensorService;
import ZAAK.backend.ZAAK_Test.alert.AlertService;
import ZAAK.backend.ZAAK_Test.machine.machineType.MachineType;
import ZAAK.backend.ZAAK_Test.machine.machineType.MachineTypeRepository;
import ZAAK.backend.ZAAK_Test.machine.sensorMetric.SensorMetricRepository;
import ZAAK.backend.ZAAK_Test.mqtt.MqttSensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final RedisSensorService redisSensorService;
    private final MachineTypeRepository machineTypeRepository;
    private final SensorMetricRepository sensorMetricRepository;

    private final AlertService alertService;
    private final Map<String, String> statusHistory = new ConcurrentHashMap<>();

    public List<MachineDetailsDto> findAllWithDetails() {

        List<Machine> machines = machineRepository.findAll();

        // 🔥 Load all machineTypes once
        Map<String, MachineType> machineTypeMap =
                machineTypeRepository.findAll().stream()
                        .collect(Collectors.toMap(MachineType::getId, mt -> mt));

        return machines.stream()
                .map(machine -> {
                    MachineType mt = machineTypeMap.get(machine.getMachineTypeId());
                    if (mt == null) {
                        throw new RuntimeException("MachineType not found: " + machine.getMachineTypeId());
                    }
                    return buildBaseDetails(machine, mt);
                })
                .toList();
    }


    public MachineDetailsDto getMachineDetailsWithMetrics(String machineId) {

        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        MachineType machineType = machineTypeRepository
                .findById(machine.getMachineTypeId())
                .orElseThrow(() -> new RuntimeException("MachineType not found"));

        // ✅ reuse base
        MachineDetailsDto base = buildBaseDetails(machine, machineType);


        // ✅ build final DTO using base
        return MachineDetailsDto.builder()
                .id(base.getId())
                .name(base.getName())
                .serialNumber(base.getSerialNumber())
                .location(base.getLocation())
                .status(base.getStatus())

                .model(base.getModel())
                .manufacturer(base.getManufacturer())
                .description(base.getDescription())

                .installDate(base.getInstallDate())
                .nextMaintenance(base.getNextMaintenance())
                .lastMaintenance(base.getLastMaintenance())

                .machineTypeId(base.getMachineTypeId())
                .machineTypeName(base.getMachineTypeName())
                .machineTypeDescription(base.getMachineTypeDescription())

                .sensors(base.getSensors())
                .build();
    }


    private MachineDetailsDto buildBaseDetails(
            Machine machine,
            MachineType machineType
    ) {
        return MachineDetailsDto.builder()
                .id(machine.getId())
                .name(machine.getName())
                .location(machine.getLocation())
                .status(machine.getStatus().name())
                .serialNumber(machine.getSerialNumber())

                .model(machine.getModel())
                .manufacturer(machine.getManufacturer())
                .description(machine.getDescription())

                .installDate(machine.getInstallDate())
                .nextMaintenance(machine.getNextMaintenance())
                .lastMaintenance(machine.getLastMaintenance())

                .machineTypeId(machineType.getId())
                .machineTypeName(machineType.getName())
                .machineTypeDescription(machineType.getDescription())

                .sensors(machineType.getMachineTypeSensors())
                .build();
    }

    public void processSensorData(MqttSensorData data) {

        redisSensorService.processReading(
                data.getMachineId(),
                data.getSensorId(),
                data.getValue()
        );
    }

    private MachineStatus parseStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        try {
            return MachineStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid status: " + status +
                            ". Allowed values: " + Arrays.toString(MachineStatus.values())
            );
        }
    }

    public Machine createMachine(CreateMachineDto request) {

        // Optional: check if already exists
        if (request.getId() != null && machineRepository.existsById(request.getId())) {
            throw new RuntimeException("Machine already exists with id: " + request.getId());
        }

        // 🔹 2. Validate machineTypeId
        if (request.getMachineTypeId() == null || request.getMachineTypeId().isEmpty()) {
            throw new IllegalArgumentException("machineTypeId is required");
        }

        MachineType machineType = machineTypeRepository
                .findById(request.getMachineTypeId())
                .orElseThrow(() -> new RuntimeException(
                        "Invalid machineTypeId: " + request.getMachineTypeId()
                ));

        // 🔹 3. Validate + normalize status
        MachineStatus status = parseStatus(request.getStatus());

        // 🔹 4. Create machine
        Machine machine = new Machine();

        machine.setId(request.getId()); // or generate UUID if null
        machine.setName(request.getName());
        machine.setSerialNumber(request.getSerialNumber());
        machine.setLocation(request.getLocation());

        machine.setStatus(status);

        machine.setInstallDate(request.getInstallDate());
        machine.setLastMaintenance(request.getLastMaintenance());
        machine.setNextMaintenance(request.getNextMaintenance());
        machine.setModel(request.getModel());
        machine.setManufacturer(request.getManufacturer());
        machine.setDescription(request.getDescription());

        machine.setMachineTypeId(machineType.getId());

        return machineRepository.save(machine);
    }
}
