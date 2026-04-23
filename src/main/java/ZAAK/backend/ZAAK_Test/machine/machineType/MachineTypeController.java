package ZAAK.backend.ZAAK_Test.machine.machineType;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machineTypes")
@RequiredArgsConstructor
public class MachineTypeController {

    private final MachineTypeService service;

    @PostMapping
    public MachineType create(@RequestBody MachineType type) {
        return service.create(type);
    }

    @GetMapping
    public List<MachineType> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MachineType getById(@PathVariable String id) {
        return service.findById(id);
    }
}