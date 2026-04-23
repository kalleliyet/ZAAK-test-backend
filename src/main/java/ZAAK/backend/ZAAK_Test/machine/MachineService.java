package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.Redis.RedisSensorService;
import ZAAK.backend.ZAAK_Test.alert.AlertService;
import ZAAK.backend.ZAAK_Test.events.EventsPublisher;
import ZAAK.backend.ZAAK_Test.machine.machineType.MachineType;
import ZAAK.backend.ZAAK_Test.machine.machineType.MachineTypeRepository;
import ZAAK.backend.ZAAK_Test.mqtt.MqttSensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final RedisSensorService redisSensorService;
    private final MachineTypeRepository machineTypeRepository;

    private final AlertService alertService;
    private final EventsPublisher publisher;
    private final Map<String, String> statusHistory = new ConcurrentHashMap<>();

    public List<Machine> findAll() {
        return machineRepository.findAll();
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
