package lk.ijse.paymentservice.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.paymentservice.dto.PaymentDto;
import lk.ijse.paymentservice.dto.ReservationDto;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.repo.PaymentRepo;
import lk.ijse.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepo paymentRepo;


    @Override
    public boolean savePayment(PaymentDto paymentDto, String authHeader) {


        paymentDto.setPaymentTime(LocalDateTime.now());

        String userUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/userIdByReservation/" + paymentDto.getReservationId();
        String vehicleUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/vehicleIdByReservation/" + paymentDto.getReservationId();
        String reservationUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/reservationGetId/" + paymentDto.getReservationId();
        String amountUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/reservationGetId/getAmount/" + paymentDto.getReservationId();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Long> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, entity, Long.class);
            Long userId = userResponse.getBody();

            ResponseEntity<Long> vehicleResponse = restTemplate.exchange(vehicleUrl, HttpMethod.GET, entity, Long.class);
            Long vehicleId = vehicleResponse.getBody();

            ResponseEntity<ReservationDto> reservationResponse = restTemplate.exchange(reservationUrl, HttpMethod.GET, entity, ReservationDto.class);
            ReservationDto reservationDto = reservationResponse.getBody();

            ResponseEntity<Double> amountResponse = restTemplate.exchange(amountUrl, HttpMethod.GET, entity, Double.class);
            Double totalAmount = amountResponse.getBody();

            if (userId != null && vehicleId != null && reservationDto != null && totalAmount != null) {
                Payment payment = new Payment();
                payment.setUserId(userId);
                payment.setVehicleId(vehicleId);
                payment.setReservationId(paymentDto.getReservationId());
                payment.setAmount(totalAmount);
                payment.setPaymentMethod(paymentDto.getPaymentMethod());
                payment.setPaymentTime(paymentDto.getPaymentTime());
                payment.setPaymentStatus("SUCCESS");

                paymentRepo.save(payment);

                //
                String completeStatusUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/complete/" + paymentDto.getReservationId();

                restTemplate.exchange(
                        completeStatusUrl,
                        HttpMethod.PUT,
                        entity,
                        String.class
                );

                //meken vehicle eka payment eka krl exit unama park wela na kiyl aye update weno vehicle table eke
                String vehicleUpdateUrl = "http://localhost:8080/vehicle-service/api/v1/vehicle/isNotParking/" + paymentDto.getVehicleId();

                restTemplate.exchange(
                        vehicleUpdateUrl,
                        HttpMethod.PUT,
                        entity,
                        String.class
                );


                // 1. get spaceId from reservationId
                String spaceIdUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/getSpaceIdByReservation/" + paymentDto.getReservationId();

                ResponseEntity<Long> spaceIdResponse = restTemplate.exchange(
                        spaceIdUrl,
                        HttpMethod.GET,
                        entity,
                        Long.class
                );

                Long spaceId = spaceIdResponse.getBody();

                if (spaceId != null) {
                    // 2. update space as available
                    String spaceUpdateUrl = "http://localhost:8080/parking-service/api/v1/parkingSpace/Available/" + spaceId;

                    restTemplate.exchange(
                            spaceUpdateUrl,
                            HttpMethod.PUT,
                            entity,
                            String.class
                    );
                }

                //vehicle status update wenwa
                String notParkedVehicle = "http://localhost:8080/vehicle-service/api/v1/vehicle/isNotParking/" + paymentDto.getVehicleId();

                restTemplate.exchange(
                        notParkedVehicle,
                        HttpMethod.PUT,
                        entity,
                        String.class
                );

                System.out.println("Payment saved successfully!");
                return true;
            } else {
                System.out.println("Missing required data for saving payment");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Optional<Payment> getTransactionById(Long id) {
        return paymentRepo.findByPaymentId(id);
    }
}


