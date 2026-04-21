package ZAAK.backend.ZAAK_Test.machine.threshold;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Threshold {

    private Double minWarningValue;
    private Double maxWarningValue;

    private Double minCriticalValue;
    private Double maxCriticalValue;
}