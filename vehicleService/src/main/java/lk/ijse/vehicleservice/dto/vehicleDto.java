package lk.ijse.vehicleservice.dto;

import lk.ijse.vehicleservice.entity.util.ParkingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class vehicleDto {
    /*private String plateNumber;
    private String brand;
    private String vehicleType;
    private String color;
    private String type;
    private boolean isParked;
    private Long userId;*/
    private Long vehicleId;
    private String plateNumber;
    private String brand;
    private String color;
    private String type;
    private ParkingStatus status;
    private Long userId;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    private String userName;
    private String phoneNumber;
}
