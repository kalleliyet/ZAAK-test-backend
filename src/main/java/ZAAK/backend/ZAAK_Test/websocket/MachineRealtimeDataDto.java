package ZAAK.backend.ZAAK_Test.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineRealtimeDataDto {
    private String machineId;
    private String status;
    private Map<String, Double> sensors;
}