package lk.ijse.parkingservice.controller;

import lk.ijse.parkingservice.dto.ReservationDto;
import lk.ijse.parkingservice.entity.Reservation;
import lk.ijse.parkingservice.repo.ReservationRepo;
import lk.ijse.parkingservice.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@RequestMapping("api/v1/parkingSpace")
@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("reservation/save")
    public ResponseEntity<?> saveReservation(
            @RequestBody ReservationDto reservationDto,
            @RequestHeader("Authorization") String authHeader) {
        System.out.println(reservationDto);
        boolean success = reservationService.saveReservation(reservationDto, authHeader);
        return success ? ResponseEntity.ok("Reservation saved") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reservation failed: No available parking space.");
    }


    @GetMapping("reservationGetId/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        Optional<Reservation> optionalReservation = reservationRepo.findById(id);
        if (optionalReservation.isPresent()) {
            ReservationDto reservationDto = modelMapper.map(optionalReservation.get(), ReservationDto.class);
            return ResponseEntity.ok(reservationDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/complete/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        Optional<Reservation> optional = reservationRepo.findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            reservation.setStatus(Reservation.Status.COMPLETED);
            reservationRepo.save(reservation);
            return ResponseEntity.ok("Reservation status Updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }
    }



    @GetMapping("reservationGetId/getAmount/{id}")
    public ResponseEntity<Double> createAmount(@PathVariable  Long id,
                                               @RequestHeader("Authorization") String authHeader) {

        double amount = reservationService.calculateAmount(id,authHeader);
        if (amount == 0.0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0.0);
        } else {
            return ResponseEntity.ok(amount);
        }
    }


    @GetMapping("vehicleIdByReservation/{vehicleId}")
    public ResponseEntity<Long> getReservationByVehicleId(@PathVariable Long vehicleId) {
        Optional<Reservation> optionalReservation = reservationRepo.findById(vehicleId);
        if (optionalReservation.isPresent()) {
            Long vehicleId1 = optionalReservation.get().getVehicleId();
            return ResponseEntity.ok(vehicleId1);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("userIdByReservation/{userId}")
    public ResponseEntity<Long> getReservationByUserId(@PathVariable Long userId) {
        Optional<Reservation> optionalReservation = reservationRepo.findById(userId);
        if (optionalReservation.isPresent()) {
            Long userIdd = optionalReservation.get().getUserId();
            return ResponseEntity.ok(userIdd);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/getSpaceIdByReservation/{reservationId}")
    public ResponseEntity<Long> getSpaceIdByReservation(@PathVariable Long reservationId) {
        Optional<Reservation> optional = reservationRepo.findById(reservationId);
        return optional.map(res -> ResponseEntity.ok(res.getSpaceId()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}
