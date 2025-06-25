package lk.ijse.vehicleservice.entity;

import jakarta.persistence.*;
import lk.ijse.vehicleservice.entity.util.ParkingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(nullable = false)
    private String plateNumber;

    @Column(nullable = false)
    private String brand;

    private String color;

    @Column(nullable = false)
    private String type;


    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ParkingStatus status = ParkingStatus.NOT_PARKED;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;


}
