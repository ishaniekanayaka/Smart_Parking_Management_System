package lk.ijse.parkingservice.service;


import lk.ijse.parkingservice.dto.ReservationDto;

public interface ReservationService {

    boolean saveReservation(ReservationDto reservationDto, String authHeader);

    double calculateAmount(Long id,String authHeader);
}
