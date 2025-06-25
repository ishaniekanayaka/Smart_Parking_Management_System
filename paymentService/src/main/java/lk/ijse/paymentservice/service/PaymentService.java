package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.PaymentDto;
import lk.ijse.paymentservice.entity.Payment;

import java.util.Optional;

public interface PaymentService {
    boolean savePayment(PaymentDto paymentDto, String authHeader);
    Optional<Payment> getTransactionById(Long id);
}
