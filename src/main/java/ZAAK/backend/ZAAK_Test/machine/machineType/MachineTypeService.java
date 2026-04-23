package ZAAK.backend.ZAAK_Test.machine.machineType;

import ZAAK.backend.ZAAK_Test.machine.sensor.Sensor;
import ZAAK.backend.ZAAK_Test.machine.sensor.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineTypeService {

    private final MachineTypeRepository repository;
    private final SensorRepository sensorRepository;

    public MachineType create(MachineType type) {

        // 🔹 1. Validate sensors list
        if (type.getMachineTypeSensors() == null || type.getMachineTypeSensors().isEmpty()) {
            throw new IllegalArgumentException("MachineType must have at least one sensor");
        }

        // 🔹 2. Validate each sensor
        for (MachineTypeSensor mts : type.getMachineTypeSensors()) {

            if (mts.getSensorId() == null || mts.getSensorId().isEmpty()) {
                throw new IllegalArgumentException("sensorId is required");
            }

            Sensor sensor = sensorRepository.findById(mts.getSensorId())
                    .orElseThrow(() -> new RuntimeException(
                            "Invalid sensorId: " + mts.getSensorId()
                    ));

            // 🔥 Optional but VERY IMPORTANT (data consistency)
            // override name/unit from DB to avoid mismatch
            mts.setSensorName(sensor.getName());
            mts.setUnit(sensor.getUnit());
        }

        // 🔹 3. Save validated MachineType
        return repository.save(type);
    }

    public List<MachineType> findAll() {
        return repository.findAll();
    }

    public MachineType findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MachineType not found"));
    }
}