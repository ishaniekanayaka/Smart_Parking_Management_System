package lk.ijse.parkingservice.repo;


import lk.ijse.parkingservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation,Long> {

}
