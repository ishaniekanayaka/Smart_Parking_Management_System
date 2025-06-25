package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    private Double amount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentTime;
    private Long UserId;
    private Long ReservationId;
    private Long vehicleId;

}
