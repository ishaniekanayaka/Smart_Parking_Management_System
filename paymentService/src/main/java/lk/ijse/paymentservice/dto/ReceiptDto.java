package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {
    private String receiptId;
    private String userName;
    private Long reservationId;
    private Double amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentTime;
}
