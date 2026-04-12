package ZAAK.backend.ZAAK_Test.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final EventsPublisher publisher;
    private final AtomicInteger clients = new AtomicInteger(0);

    @EventListener
    public void onConnect(SessionConnectedEvent event) {
        int count = clients.incrementAndGet();
        publisher.sendClientCount(count);

        System.out.println("Client connected → total: " + count);
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        int count = clients.decrementAndGet();
        publisher.sendClientCount(count);

        System.out.println("Client disconnected → total: " + count);
    }
}