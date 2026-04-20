package ZAAK.backend.ZAAK_Test.machine;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    public Machine createMachine(@RequestBody CreateMachineDto request) {
        return machineService.createMachine(request);
    }

    @GetMapping
    public List<Machine> getAllMachines() {
        return machineService.findAll();
    }
}