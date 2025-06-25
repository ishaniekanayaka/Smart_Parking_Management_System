package lk.ijse.parkingservice.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.parkingservice.dto.Parking_spaceDTO;
import lk.ijse.parkingservice.dto.ReservationDto;
import lk.ijse.parkingservice.dto.UserDto;
import lk.ijse.parkingservice.dto.vehicleDto;
import lk.ijse.parkingservice.entity.Parking_Space;
import lk.ijse.parkingservice.entity.Reservation;
import lk.ijse.parkingservice.repo.ParkingRepo;
import lk.ijse.parkingservice.repo.ReservationRepo;
import lk.ijse.parkingservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ParkingRepo parkingRepo;
    @Autowired
    private ReservationRepo reservationRepo;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public boolean saveReservation(ReservationDto reservationDto, String authHeader) {

        System.out.println("reservation ekata awa");
        List<Parking_Space> existing = parkingRepo.findAllByIsAvailableTrue();
        System.out.println("--------------------------"+existing);

        if (existing.isEmpty()) {
            System.out.println("Not Available");
            return false;
        }

        System.out.println("user id" + reservationDto.getUserId());

        String user = "http://localhost:8080/user-service/api/v1/user/" + reservationDto.getUserId();
        String vehicle = "http://localhost:8080/vehicle-service/api/v1/vehicle/" + reservationDto.getVehicleId();
        String space = "http://localhost:8080/parking-service/api/v1/parkingSpace/" + reservationDto.getSpaceId();


        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDto> response = restTemplate.exchange(
                    user,
                    HttpMethod.GET,
                    entity,
                    UserDto.class
            );
            ResponseEntity<vehicleDto> response2 = restTemplate.exchange(
                    vehicle,
                    HttpMethod.GET,
                    entity,
                    vehicleDto.class
            );

            ResponseEntity<Parking_spaceDTO> response3 = restTemplate.exchange(
                    space,
                    HttpMethod.GET,
                    entity,
                    Parking_spaceDTO.class
            );


            if (response.getStatusCode().is2xxSuccessful()) {
                UserDto userDto = response.getBody();
                vehicleDto vehicleDto = response2.getBody();
                Parking_spaceDTO parkingSpaceDTO = response3.getBody();



                if (userDto != null && userDto.isActive()  & parkingSpaceDTO != null && parkingSpaceDTO.isAvailable()) {
                    Reservation reservation = new Reservation();
                    reservation.setEndTime(reservationDto.getEndTime());
                    reservation.setStartTime(reservationDto.getStartTime());
                    reservation.setStatus(Reservation.Status.ACTIVE);
                    reservation.setSpaceId(reservationDto.getSpaceId());
                    reservation.setUserId(reservationDto.getUserId());
                    reservation.setVehicleId(reservationDto.getVehicleId());
                    System.out.println(reservation);
                    reservationRepo.save(reservation);

                    Optional<Parking_Space> optionalParkingSpace = parkingRepo.findById(reservationDto.getSpaceId());
                    if (optionalParkingSpace.isPresent()) {
                        Parking_Space parkingSpace = optionalParkingSpace.get();
                        parkingSpace.setAvailable(false);
                        parkingRepo.save(parkingSpace);
                    }

                    //vehicle eka park
                    String park = "http://localhost:8080/vehicle-service/api/v1/vehicle/Parking/" + reservationDto.getVehicleId();

                    restTemplate.exchange(
                            park,
                            HttpMethod.PUT,
                            entity,
                            String.class
                    );

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public double calculateAmount(Long reservationId, String authHeader) {
        Optional<Reservation> optional = reservationRepo.findById(reservationId);

        if (optional.isEmpty()) {
            return 0.0;
        }

        try {

            Reservation reservationn = optional.get();//obj eka ganne meken

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String vehicleUrl = "http://localhost:8080/vehicle-service/api/v1/vehicle/" + reservationn.getVehicleId();
            ResponseEntity<vehicleDto> vehicleResponse = restTemplate.exchange(
                    vehicleUrl,
                    HttpMethod.GET,
                    entity,
                    vehicleDto.class
            );

            if (!vehicleResponse.getStatusCode().is2xxSuccessful() || vehicleResponse.getBody() == null) {
                return 0.0;
            }

            vehicleDto vehicle = vehicleResponse.getBody();
            System.out.println("Vehicle Details: " + vehicle);

            Reservation reservation = optional.get();
            LocalDateTime start = reservation.getStartTime();
            LocalDateTime end = reservation.getEndTime();


            long hours = Duration.between(start, end).toHours();
            if (hours == 0) {
                hours = 1;
            }

            System.out.println("Total parking hours: " + hours);


            double totalAmount;

            switch (vehicle.getType().toLowerCase()) {
                case "car":
                    totalAmount = hours * 700;
                    break;
                case "van":
                    totalAmount = hours * 800;
                    break;
                case "bike":
                    totalAmount = hours * 300;
                    break;
                case "3wheel":
                    totalAmount = hours * 500;
                    break;
                default:
                    totalAmount = hours * 1000;
                    break;
            }

            return totalAmount;


        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}