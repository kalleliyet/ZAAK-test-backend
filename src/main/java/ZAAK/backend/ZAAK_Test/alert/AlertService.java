package ZAAK.backend.ZAAK_Test.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<Alert> findAll() {
        return alertRepository.findByVisibleTrueOrderByTimestampDesc();
    }

    public Alert create(CreateAlertDto dto) {

        // Simplified: find existing manually (instead of Mongo upsert)
        Optional<Alert> existingOpt = alertRepository.findAll().stream()
                .filter(a ->
                        a.getMachineId().equals(dto.getMachineId()) &&
                                a.getCategory().equals(dto.getCategory()) &&
                                a.getSeverity().equals(dto.getSeverity()) &&
                                Objects.equals(a.getSensor(), dto.getSensor())
                )
                .findFirst();

        Alert alert;

        if (existingOpt.isPresent()) {
            alert = existingOpt.get();
            alert.setMessage(dto.getMessage());
            alert.setMachineName(dto.getMachineName());
            alert.setSensorValue(dto.getSensorValue());
            alert.setTimestamp(new Date());
            alert.setVisible(true);
        } else {
            alert = new Alert();
            alert.setMachineId(dto.getMachineId());
            alert.setMachineName(dto.getMachineName());
            alert.setMachineType(dto.getMachineType());
            alert.setCategory(dto.getCategory());
            alert.setSeverity(dto.getSeverity());
            alert.setSensor(dto.getSensor());
            alert.setResolutionNotes(dto.getResolutionNotes() != null ? dto.getResolutionNotes() : new ArrayList<>());
            alert.setMessage(dto.getMessage());
            alert.setSensorValue(dto.getSensorValue());
        }

        return alertRepository.save(alert);
    }

    public Alert resolveAlert(String id, String note) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setVisible(false);

        if (alert.getResolutionNotes() == null) {
            alert.setResolutionNotes(new ArrayList<>());
        }

        alert.getResolutionNotes().add(note);

        return alertRepository.save(alert);
    }

    public Map<String, Boolean> deleteById(String id) {
        alertRepository.deleteById(id);
        return Map.of("deleted", true);
    }
}