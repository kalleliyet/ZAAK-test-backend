package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.alert.AlertService;
import ZAAK.backend.ZAAK_Test.events.EventsPublisher;
import ZAAK.backend.ZAAK_Test.mqtt.MqttSensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;

    private final AlertService alertService;
    private final EventsPublisher publisher;
   // private final Map<String, MachineRealtimePayload> machinesData = new ConcurrentHashMap<>();
    private final Map<String, String> statusHistory = new ConcurrentHashMap<>();

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public Machine createFromSensorData(MqttSensorData dto) {

        if (dto.getMachineId() == null) {
            throw new IllegalArgumentException("MachineId is required");
        }

        return machineRepository.findById(dto.getMachineId())
                .orElseGet(() -> machineRepository.save(toMachine(dto)));
    }

    public void processSensorData(MqttSensorData data) {
        // Create machine if not found in BD
        Machine machine = createFromSensorData(data);
    }

    private Machine toMachine(MqttSensorData dto) {
        Machine machine = new Machine();
        machine.setId(dto.getMachineId());
        machine.setName(dto.getName());
        machine.setLocation(dto.getLocation());
        machine.setStatus(dto.getStatus());
        machine.setModel(dto.getModel());
        machine.setManufacturer(dto.getManufacturer());
        machine.setDescription(dto.getDescription());
        return machine;
    }
}
