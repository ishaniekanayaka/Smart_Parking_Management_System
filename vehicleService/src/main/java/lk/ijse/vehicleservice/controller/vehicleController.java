package lk.ijse.vehicleservice.controller;


import lk.ijse.vehicleservice.dto.vehicleDto;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.entity.util.ParkingStatus;
import lk.ijse.vehicleservice.repo.VehicleRepo;
import lk.ijse.vehicleservice.service.VehicleServive;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/vehicle")
public class vehicleController {

    @Autowired
    private VehicleServive vehicleServive;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private ModelMapper modelMapper;



    @PostMapping("/save")
    public ResponseEntity<?> registerVehicle(
            @RequestBody vehicleDto vehicleDto,
            @RequestHeader("Authorization") String authHeader) {
        System.out.println(vehicleDto);
        boolean success = vehicleServive.registerVehicle(vehicleDto, authHeader);
        return success ? ResponseEntity.ok("Vehicle registered") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vehicle registration failed");
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVehicle(@RequestBody vehicleDto vehicleDto,
                                           @PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader){

        boolean success = vehicleServive.updateVehicle(vehicleDto, id,authHeader);
        return success ? ResponseEntity.ok("Vehicle Updated") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vehicle update failed");

    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader){

        boolean success = vehicleServive.deleteVehicle(id,authHeader);
        return success ? ResponseEntity.ok("Vehicle deleted") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vehicle delete failed");

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<vehicleDto>> getAllVehicles(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(vehicleServive.getAllVehicles(authHeader));
    }



    /*@GetMapping("/getAll")
    public ResponseEntity<List<vehicleDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleServive.getAllVehicles());
    }*/


    //get Vehicle Id
    @GetMapping("/{id}")
    public ResponseEntity<vehicleDto> getUserById(@PathVariable Long id) {
        Optional<Vehicle> user = vehicleRepo.findById(id);
        if (user.isPresent()) {
            vehicleDto vehicleDto = modelMapper.map(user.get(), vehicleDto.class);
            return ResponseEntity.ok(vehicleDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


//http://localhost:8081/vehicle-service/api/v1/vehicle/isParking
    /*@PutMapping("/isParking/{id}")
    public ResponseEntity<?> updateIsParking(@PathVariable Long id) {
        Optional<Vehicle> optional = vehicleRepo.findById(id);
        if (optional.isPresent()) {
            Vehicle reservation = optional.get();
            reservation.setStatus(ParkingStatus.PARKED);
            vehicleRepo.save(reservation);
            return ResponseEntity.ok("Reservation status Updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }
    }*/
@PutMapping("/isParking/{id}")
public ResponseEntity<?> updateIsParking(@PathVariable Long id) {
    Optional<Vehicle> optional = vehicleRepo.findById(id);
    if (optional.isPresent()) {
        Vehicle vehicle = optional.get();
        vehicle.setStatus(ParkingStatus.PARKED);
        vehicle.setEntryTime(LocalDateTime.now()); // ⏰ Entry set here
        vehicleRepo.save(vehicle);
        return ResponseEntity.ok("Vehicle status updated to PARKED with entryTime");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
    }
}



   /* @PutMapping("/isNotParking/{id}")
    public ResponseEntity<?> updateIsNotParking(@PathVariable Long id) {
        Optional<Vehicle> optional = vehicleRepo.findById(id);
        if (optional.isPresent()) {
            Vehicle vehicle = optional.get();
            vehicle.setStatus(ParkingStatus.NOT_PARKED);
            vehicleRepo.save(vehicle);
            return ResponseEntity.ok("Vehicle status updated to NOT_PARKED");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
    }
*/
   @PutMapping("/isNotParking/{id}")
   public ResponseEntity<?> updateIsNotParking(@PathVariable Long id) {
       Optional<Vehicle> optional = vehicleRepo.findById(id);
       if (optional.isPresent()) {
           Vehicle vehicle = optional.get();
           vehicle.setStatus(ParkingStatus.NOT_PARKED);
           vehicle.setExitTime(LocalDateTime.now()); // ⏰ Exit set here
           vehicleRepo.save(vehicle);
           return ResponseEntity.ok("Vehicle status updated to NOT_PARKED with exitTime");
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
       }
   }


    @PutMapping("/Parking/{id}")
    public ResponseEntity<?> updateParking(@PathVariable Long id) {
        Optional<Vehicle> optional = vehicleRepo.findById(id);
        if (optional.isPresent()) {
            Vehicle vehicle = optional.get();
            vehicle.setStatus(ParkingStatus.PARKED);
            vehicleRepo.save(vehicle);
            return ResponseEntity.ok("Vehicle status updated to PARKED");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
    }




}



