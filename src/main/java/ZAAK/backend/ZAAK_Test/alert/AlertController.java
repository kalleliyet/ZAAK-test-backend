package ZAAK.backend.ZAAK_Test.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<Alert> findAll() {
        return alertService.findAll();
    }

    @PostMapping
    public Alert create(@RequestBody CreateAlertDto dto) {
        return alertService.create(dto);
    }

    @PatchMapping("/{id}/resolve")
    public Alert resolve(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        return alertService.resolveAlert(id, body.getOrDefault("resolutionNote", ""));
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable String id) {
        return alertService.deleteById(id);
    }
}