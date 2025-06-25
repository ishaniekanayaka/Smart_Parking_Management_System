package lk.ijse.parkingservice.service;

import lk.ijse.parkingservice.dto.Parking_spaceDTO;
import lk.ijse.parkingservice.entity.Parking_Space;

import java.util.List;

public interface Parking_spaceService {

    /*boolean saveParkingSpace(Parking_spaceDTO parkingSpaceDTO, String authHeader);
    boolean updateSpace(Parking_spaceDTO parkingSpaceDTO,Long id,String authHeader);
    boolean deleteSpace(Long id,String authHeader);
    List<Parking_Space> getAllSpaces();
*/

    boolean saveParkingSpace(Parking_spaceDTO parkingSpaceDTO, String authHeader);

    boolean updateSpace(Parking_spaceDTO parkingSpaceDTO, Long id, String authHeader);

    boolean deleteSpace(Long id, String authHeader);

    List<Parking_spaceDTO> getAllSpaces();

    Parking_spaceDTO getSpaceById(Long id);

    boolean updateAvailability(Long id, boolean available);
}
