package ZAAK.backend.ZAAK_Test.mqtt;

import ZAAK.backend.ZAAK_Test.machine.MachineService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqttService {

    private final MachineService machineService; // 🔥 inject your core logic
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MqttClient client;

    private final String MQTT_URL = System.getenv().getOrDefault("MQTT_URL", "tcp://localhost:1884");
    private final String TOPIC = "factory/machine/+/sensos";

    @PostConstruct
    public void init() {
        try {
            client = new MqttClient(MQTT_URL, MqttClient.generateClientId());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);

            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    log.warn("MQTT connection lost: {}", cause.getMessage(), cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    handleMessage(topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            });

            client.connect(options);

            log.info("Connected to MQTT broker: {}", MQTT_URL);

            client.subscribe(TOPIC, (topic, message) -> handleMessage(topic, message));

            log.info("Subscribed to topic: {}", TOPIC);

        } catch (Exception e) {
            log.error("MQTT connection failed: {}", e.getMessage(), e);
        }
    }

    private void handleMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            log.info("MQTT raw message received. topic={} qos={} retained={} payload={}",
                    topic, message.getQos(), message.isRetained(), payload);

            // 🔥 Extract machineId from topic
            String[] parts = topic.split("/");
            String machineIdFromTopic = parts.length > 2 ? parts[2] : null;

            // 🔥 Parse JSON (use Jackson)
            JsonNode node = objectMapper.readTree(payload);

            String resolvedId =
                    machineIdFromTopic != null ? machineIdFromTopic :
                            node.has("machineId") ? node.get("machineId").asText() :
                                    node.has("id") ? node.get("id").asText() :
                                            null;

            if (resolvedId == null) {
                log.warn("Ignored MQTT message without machineId. topic={} payload={}", topic, payload);
                return;
            }

            MqttSensorData data = new MqttSensorData();
            data.setMachineId(resolvedId);

            if (node.has("name")) data.setName(node.get("name").asText());
            if (node.has("type")) data.setType(node.get("type").asText());
            if (node.has("temperature")) data.setTemperature(node.get("temperature").asDouble());
            if (node.has("vibration")) data.setVibration(node.get("vibration").asDouble());
            if (node.has("pressure")) data.setPressure(node.get("pressure").asDouble());
            if (node.has("status")) data.setStatus(node.get("status").asText());

            data.setTimestamp(System.currentTimeMillis());

            // 🔥 CORE PIPELINE (same as NestJS Subject.next())
            log.info("MQTT normalized data: {}", data);
            machineService.processSensorData(data);

        } catch (Exception e) {
            log.error("Failed to parse MQTT message from topic={}: {}", topic, e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                log.info("MQTT disconnected");
            }
        } catch (Exception e) {
            log.error("Error during MQTT shutdown: {}", e.getMessage(), e);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}
