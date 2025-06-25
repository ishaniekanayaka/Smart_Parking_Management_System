package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.Parking_Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingRepo extends JpaRepository<Parking_Space,Long> {
    List<Parking_Space> findAllByIsAvailableTrue();

}
