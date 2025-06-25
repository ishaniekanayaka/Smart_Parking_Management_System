package lk.ijse.vehicleservice.repo;


import lk.ijse.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Long> {
    Optional<Vehicle> findByPlateNumber(String plateNumber);

}
