package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.events.EventsPublisher;
import ZAAK.backend.ZAAK_Test.alert.AlertService;
import ZAAK.backend.ZAAK_Test.mqtt.MqttSensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MachineService {

    private final MachineRegistry registry;
    private final AlertService alertService;
    private final EventsPublisher publisher;
   // private final Map<String, MachineRealtimePayload> machinesData = new ConcurrentHashMap<>();
    private final Map<String, String> statusHistory = new ConcurrentHashMap<>();

    public /*MachineRealtimePayload*/ void processSensorData(MqttSensorData data) {
        log.info("Processing MQTT sensor data in MachineService: {}", data);

        /*Machine machine = registry.getOrRegister(data);

        UpdateResult result = machine.updateSensorData(data.toReading(), data.getStatus());

        MachineRealtimePayload payload = buildPayload(machine, data, result);

        machinesData.put(machine.getId(), payload);

        // 🔥 ALERT LOGIC
        handleAlerts(machine, result);

        // 🔥 WEBSOCKET BROADCAST
        publisher.sendMachineUpdate(getAllRealtime());
        return payload;*/
    }

    private void handleAlerts(Machine machine, UpdateResult result) {
  /*      String prev = statusHistory.get(machine.getId());
        String current = result.getStatus().name();

        if (!Objects.equals(prev, current)) {
            result.getDiagnostics().forEach(diag -> {
                if (!diag.getStatus().equalsIgnoreCase("normal")) {

                    var alert = alertService.createFromDiagnostic(machine, diag);

                    // 🔥 SEND TO FRONTEND
                    publisher.sendNewAlert(alert);
                }
            });
        }

        statusHistory.put(machine.getId(), current);*/
    }

    private MachineRealtimePayload buildPayload(Machine machine, MqttSensorData data, UpdateResult result) {
        System.out.println("data buildPayload is done");

        MachineRealtimePayload payload = new MachineRealtimePayload();
        payload.setId(machine.getId());
        payload.setName(machine.getName());
        payload.setType(machine.getType());
        payload.setTemperature(data.getTemperature());
        payload.setVibration(data.getVibration());
        payload.setPressure(data.getPressure());
        payload.setStatus(result.getStatus().name());
        payload.setTimestamp(String.valueOf(data.getTimestamp()));
        payload.setDiagnostics(result.getDiagnostics());
        return payload;
    }

    public /*List<MachineRealtimePayload>*/ void getAllRealtime() {
      //  return new ArrayList<>(machinesData.values());
    }
}
