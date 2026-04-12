package ZAAK.backend.ZAAK_Test.events;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventsPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMachineUpdate(Object payload) {
        messagingTemplate.convertAndSend("/topic/machineUpdate", payload);
    }

    public void sendNewAlert(Object alert) {
        messagingTemplate.convertAndSend("/topic/newAlert", alert);
    }

    public void sendClientCount(int count) {
        messagingTemplate.convertAndSend("/topic/clientCount", count);
    }
}