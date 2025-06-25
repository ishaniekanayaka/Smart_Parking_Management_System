package lk.ijse.paymentservice.repo;



import lk.ijse.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
    boolean existsByReservationId(Long reservationId);
    Optional<Payment> findByPaymentId(Long paymentId);

}
