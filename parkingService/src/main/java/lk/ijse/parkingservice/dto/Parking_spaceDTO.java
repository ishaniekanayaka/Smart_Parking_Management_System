package lk.ijse.parkingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Parking_spaceDTO {
    private String location;
    private boolean isAvailable = true;
    private String zone;
    private Long userId;
}
