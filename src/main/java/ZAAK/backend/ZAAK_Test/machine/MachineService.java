package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.Redis.RedisSensorService;
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
    private final RedisSensorService redisSensorService;

    private final AlertService alertService;
    private final EventsPublisher publisher;
    private final Map<String, String> statusHistory = new ConcurrentHashMap<>();

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public void processSensorData(MqttSensorData data) {
        // log.info(String.valueOf(data));

        // Example: assuming one sensor per message
        redisSensorService.processReading(
                data.getMachineId(),
                data.getSensorId(),
                data.getValue()
        );
    }
    public Machine createMachine(CreateMachineDto request) {

        // Optional: check if already exists
        if (request.getId() != null && machineRepository.existsById(request.getId())) {
            throw new RuntimeException("Machine already exists with id: " + request.getId());
        }

        Machine machine = new Machine();

        machine.setId(request.getId()); // or generate UUID if null
        machine.setName(request.getName());
        machine.setLocation(request.getLocation());
        machine.setStatus(request.getStatus());
        machine.setModel(request.getModel());
        machine.setManufacturer(request.getManufacturer());
        machine.setDescription(request.getDescription());
        machine.setMachineTypeId(request.getMachineTypeId());

        return machineRepository.save(machine);
    }
    private Machine toMachine(MqttSensorData dto) {
        Machine machine = new Machine();
        machine.setId(dto.getMachineId());
        /*
        machine.setName(dto.getName());
        machine.setLocation(dto.getLocation());
        machine.setStatus(dto.getStatus());
        machine.setModel(dto.getModel());
        machine.setManufacturer(dto.getManufacturer());
        machine.setDescription(dto.getDescription());
        */
        return machine;
    }
}
