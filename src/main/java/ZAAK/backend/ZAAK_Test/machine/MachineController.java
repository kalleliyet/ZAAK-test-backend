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
    @GetMapping("/{id}/details")
    public MachineDetailsWithMetrics getMachineDetails(@PathVariable String id) {
        return machineService.getMachineDetailsWithMetrics(id);
    }
    @GetMapping
    public List<MachineDetailsDto> getAllMachines() {
        return machineService.findAllWithDetails();
    }
}