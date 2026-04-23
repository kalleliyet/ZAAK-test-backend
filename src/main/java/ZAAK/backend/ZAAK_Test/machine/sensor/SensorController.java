package ZAAK.backend.ZAAK_Test.machine.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public Sensor create(@RequestBody Sensor sensor) {
        return sensorService.create(sensor);
    }

    @GetMapping
    public List<Sensor> getAll() {
        return sensorService.findAll();
    }

    @GetMapping("/{id}")
    public Sensor getById(@PathVariable String id) {
        return sensorService.findById(id);
    }
}