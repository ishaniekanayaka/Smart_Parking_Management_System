package lk.ijse.parkingservice.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Parking_Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceId;
    private String location;
    private boolean isAvailable = true;
    private String zone;
    private Long userId;

}
