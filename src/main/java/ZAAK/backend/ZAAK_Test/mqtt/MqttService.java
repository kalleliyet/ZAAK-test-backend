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
import java.util.Date;
import java.util.Iterator;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqttService {

    private final MachineService machineService; // 🔥 inject your core logic
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MqttClient client;

    private final String MQTT_URL = System.getenv().getOrDefault("MQTT_URL", "tcp://localhost:1884");
    private final String TOPIC = "factory/machine/+/sensors";

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

            client.subscribe(TOPIC);

            log.info("Subscribed to topic: {}", TOPIC);

        } catch (Exception e) {
            log.error("MQTT connection failed: {}", e.getMessage(), e);
        }
    }

    private void handleMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);

            // 🔥 Extract machineId from topic
            String[] parts = topic.split("/");
            String machineIdFromTopic = parts.length > 2 ? parts[2] : null;

            // 🔥 Parse JSON (use Jackson)
            JsonNode node = objectMapper.readTree(payload);

            String resolvedId = machineIdFromTopic != null
                    ? machineIdFromTopic
                    : firstText(node, "machineId", "id");

            if (resolvedId == null) {
                log.warn("Ignored MQTT message without machineId. topic={} payload={}", topic, payload);
                return;
            }

            // Extract sensor data
            String sensorId = firstText(node, "sensorId");
            Double value = firstDouble(node, "value");

            // Validate required fields
            if (sensorId == null || value == null) {
                log.warn("Invalid MQTT message (missing sensorId/value). topic={} payload={}", topic, payload);
                return;
            }

            // Build DTO
            MqttSensorData data = new MqttSensorData();
            data.setMachineId(resolvedId);
            data.setSensorId(sensorId);
            data.setValue(value);
            data.setTimestamp(new Date());

            // 🔥 CORE PIPELINE (same as NestJS Subject.next())
            machineService.processSensorData(data);

        } catch (Exception e) {
            log.error("Failed to parse MQTT message from topic={}: {}", topic, e.getMessage(), e);
        }
    }

    private String firstText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode match = findField(node, fieldName);
            if (match != null && !match.isNull()) {
                if (match.isValueNode()) {
                    return match.asText();
                }
                return match.toString();
            }
        }
        return null;
    }

    private Double firstDouble(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode match = findField(node, fieldName);
            if (match != null && match.isNumber()) {
                return match.asDouble();
            }
            if (match != null && match.isTextual()) {
                try {
                    return Double.parseDouble(match.asText());
                } catch (NumberFormatException ignored) {
                    // Ignore invalid numeric text and continue searching.
                }
            }
        }
        return null;
    }

    private Object firstTimestamp(JsonNode node) {
        JsonNode match = findField(node, "timestamp");
        if (match == null || match.isNull()) {
            return System.currentTimeMillis();
        }
        if (match.isNumber()) {
            return match.numberValue();
        }
        if (match.isValueNode()) {
            return match.asText();
        }
        return match.toString();
    }

    private JsonNode findField(JsonNode node, String fieldName) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isObject()) {
            JsonNode direct = node.get(fieldName);
            if (direct != null) {
                return direct;
            }

            Iterator<JsonNode> children = node.elements();
            while (children.hasNext()) {
                JsonNode match = findField(children.next(), fieldName);
                if (match != null) {
                    return match;
                }
            }
        } else if (node.isArray()) {
            for (JsonNode child : node) {
                JsonNode match = findField(child, fieldName);
                if (match != null) {
                    return match;
                }
            }
        }

        return null;
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
