package ZAAK.backend.ZAAK_Test.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Map<String, Double>> machineCache = new ConcurrentHashMap<>();

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSensorUpdate(String machineId, String sensorId, double value) {

        machineCache.putIfAbsent(machineId, new ConcurrentHashMap<>());
        machineCache.get(machineId).put(sensorId, value);

        MachineRealtimeDataDto dto = new MachineRealtimeDataDto();
        dto.setMachineId(machineId);
        dto.setStatus("running");
        dto.setSensors(new HashMap<>(machineCache.get(machineId)));

        messagingTemplate.convertAndSend("/topic/machines", dto);
    }
}