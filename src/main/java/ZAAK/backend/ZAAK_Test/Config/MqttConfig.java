package ZAAK.backend.ZAAK_Test.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageProducer;

@Configuration
public class MqttConfig {

    @Bean
    public MessageProducer mqttInbound() {
        // configure broker (tcp://localhost:1883)
        // subscribe to: factory/machine/+/sensors
        return null; // simplified for now
    }
}