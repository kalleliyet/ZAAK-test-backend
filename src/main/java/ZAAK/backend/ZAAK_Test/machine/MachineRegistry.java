package ZAAK.backend.ZAAK_Test.machine;

import ZAAK.backend.ZAAK_Test.machine.Machine;
import ZAAK.backend.ZAAK_Test.machine.subclasses.Compressor;
import ZAAK.backend.ZAAK_Test.machine.subclasses.HydraulicPump;
import ZAAK.backend.ZAAK_Test.machine.types.MachineSpec;
import ZAAK.backend.ZAAK_Test.mqtt.MqttSensorData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MachineRegistry {

    private final Map<String, Machine> machines = new ConcurrentHashMap<>();

    public Machine get(String id) {
        return machines.get(id);
    }

    public List<Machine> getAll() {
        return new ArrayList<>(machines.values());
    }

    public Machine register(MachineSpec spec) {
        Machine machine = createMachine(spec);
        machines.put(spec.getId(), machine);
        return machine;
    }

    private Machine createMachine(MachineSpec spec) {
        String type = spec.getType().toLowerCase();

        return switch (type) {
            case "compressor" -> new Compressor(spec);
            case "hydraulic pump" -> new HydraulicPump(spec);
            default -> new Compressor(spec);
        };
    }

    public void getOrRegister(MqttSensorData data) {
        System.out.println("get or register done");
    };
}