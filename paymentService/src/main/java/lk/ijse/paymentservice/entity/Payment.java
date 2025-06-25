package lk.ijse.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Double amount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentTime;
    private Long UserId;
    @Column(name = "reservation_id")
    private Long reservationId;
    private Long vehicleId;
}
