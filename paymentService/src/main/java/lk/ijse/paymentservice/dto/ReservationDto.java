package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Long spaceId;
    private Long vehicleId;
    private Long userId;
}
