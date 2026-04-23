package ZAAK.backend.ZAAK_Test.machine.sensor;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SensorRepository extends MongoRepository<Sensor, String> {

    Optional<Sensor> findByName(String name);

    boolean existsByName(String name);
}