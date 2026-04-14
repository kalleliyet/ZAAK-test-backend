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

        return machineRepository.findById(dto.getMachineId())
                .orElseGet(() -> {
                    machineRepository.save(toMachine(dto));
                    log.info("creating: {}", dto);
                    return null;
                });

    }

    public void processSensorData(MqttSensorData data) {
        // Create machine if not found in BD
        Machine machine = createFromSensorData(data);



        // 🔥 ALERT LOGIC
        handleAlerts(machine, result);


        /*
        // 🔥 WEBSOCKET BROADCAST
        publisher.sendMachineUpdate(getAllRealtime());
        return payload;*/
    }

    public /*List<MachineRealtimePayload>*/ void getAllRealtime() {
      //  return new ArrayList<>(machinesData.values());
    }

    private Machine toMachine(MqttSensorData dto) {
        Machine machine = new Machine();
        machine.setMachineId(dto.getMachineId());
        machine.setName(dto.getName());
        machine.setType(dto.getType());
        machine.setLocation(dto.getLocation());
        machine.setTemperature(dto.getTemperature());
        machine.setVibration(dto.getVibration());
        machine.setPressure(dto.getPressure());
        machine.setStatus(dto.getStatus());
        machine.setLastMaintenance(dto.getLastMaintenance());
        machine.setNextMaintenance(dto.getNextMaintenance());
        machine.setInstallDate(dto.getInstallDate());
        machine.setModel(dto.getModel());
        machine.setManufacturer(dto.getManufacturer());
        machine.setDescription(dto.getDescription());
        return machine;
    }
}
