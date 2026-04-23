package ZAAK.backend.ZAAK_Test.machine.machineType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MachineTypeRepository extends MongoRepository<MachineType, String> {

    Optional<MachineType> findByName(String name);

    boolean existsByName(String name);
}