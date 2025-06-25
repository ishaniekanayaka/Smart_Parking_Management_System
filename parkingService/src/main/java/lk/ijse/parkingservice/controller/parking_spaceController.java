package lk.ijse.parkingservice.controller;

import lk.ijse.parkingservice.dto.Parking_spaceDTO;
import lk.ijse.parkingservice.entity.Parking_Space;
import lk.ijse.parkingservice.repo.ParkingRepo;
import lk.ijse.parkingservice.service.Parking_spaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/parkingSpace")
public class parking_spaceController {

    @Autowired
    private Parking_spaceService parkingSpaceService;

    @Autowired
    private ParkingRepo parkingRepo;

    @Autowired
    private ModelMapper modelMapper;

   /* @GetMapping("all")
    public String getParking(){
        return "parking service";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveParkingSpace(
            @RequestBody Parking_spaceDTO parkingSpaceDTO,
            @RequestHeader("Authorization") String authHeader) {
        System.out.println(parkingSpaceDTO);
        boolean success = parkingSpaceService.saveParkingSpace(parkingSpaceDTO, authHeader);
        return success ? ResponseEntity.ok("Parking Space registered") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parking Space registration failed");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateParkingSpace(@RequestBody Parking_spaceDTO parkingSpaceDTO,
                                           @PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader){

        boolean success = parkingSpaceService.updateSpace(parkingSpaceDTO, id,authHeader);
        return success ? ResponseEntity.ok("space Updated") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("space update failed");

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteParkingSpace(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader){

        boolean success = parkingSpaceService.deleteSpace(id,authHeader);
        return success ? ResponseEntity.ok("ParkingSpace deleted") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("space delete failed");

    }

    @GetMapping("/getAll")
    public List<Parking_Space> getAllSpaces(){
        return parkingSpaceService.getAllSpaces();
    }

    @PutMapping("/Available/{id}")
    public ResponseEntity<?> updateStatusAvailable(@PathVariable Long id) {
        Optional<Parking_Space> optional = parkingRepo.findById(id);
        if (optional.isPresent()) {
            Parking_Space space = optional.get();
            space.setAvailable(true);
            parkingRepo.save(space);
            return ResponseEntity.ok("Status updated to Available");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking space not found");
        }
    }


    @PutMapping("/NotAvailable/{id}")
    public ResponseEntity<?> updateStatusNotAvailable(@PathVariable Long id) {
        Optional<Parking_Space> optional = parkingRepo.findById(id);
        if (optional.isPresent()) {
            Parking_Space space = optional.get();
            space.setAvailable(false);
            parkingRepo.save(space);
            return ResponseEntity.ok("Status updated to Not_Available");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking space not found");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Parking_spaceDTO> getSpaceById(@PathVariable Long id) {
        Optional<Parking_Space> user = parkingRepo.findById(id);
        if (user.isPresent()) {
            Parking_spaceDTO parkingSpaceDTO = modelMapper.map(user.get(), Parking_spaceDTO.class);
            return ResponseEntity.ok(parkingSpaceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
*/
   @GetMapping("/ping")
   public ResponseEntity<String> ping() {
       return ResponseEntity.ok("Parking service is running.");
   }

    @PostMapping
    public ResponseEntity<?> saveParkingSpace(@RequestBody Parking_spaceDTO dto,
                                              @RequestHeader("Authorization") String authHeader) {
        boolean success = parkingSpaceService.saveParkingSpace(dto, authHeader);
        return success ? ResponseEntity.ok("Parking space registered successfully")
                : ResponseEntity.badRequest().body("Failed to register parking space.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParkingSpace(@PathVariable Long id,
                                                @RequestBody Parking_spaceDTO dto,
                                                @RequestHeader("Authorization") String authHeader) {
        boolean success = parkingSpaceService.updateSpace(dto, id, authHeader);
        return success ? ResponseEntity.ok("Parking space updated successfully")
                : ResponseEntity.badRequest().body("Failed to update parking space.");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteParkingSpace(@PathVariable Long id,
                                                @RequestHeader("Authorization") String authHeader) {
        boolean success = parkingSpaceService.deleteSpace(id, authHeader);
        return success ? ResponseEntity.ok("Parking space deleted successfully")
                : ResponseEntity.badRequest().body("Failed to delete parking space.");
    }

    @GetMapping
    public ResponseEntity<List<Parking_spaceDTO>> getAllSpaces() {
        List<Parking_spaceDTO> list = parkingSpaceService.getAllSpaces();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parking_spaceDTO> getSpaceById(@PathVariable Long id) {
        Parking_spaceDTO dto = parkingSpaceService.getSpaceById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status/available")
    public ResponseEntity<String> markAsAvailable(@PathVariable Long id) {
        boolean success = parkingSpaceService.updateAvailability(id, true);
        return success ? ResponseEntity.ok("Marked as available")
                : ResponseEntity.badRequest().body("Parking space not found");
    }

    @PutMapping("/{id}/status/unavailable")
    public ResponseEntity<String> markAsUnavailable(@PathVariable Long id) {
        boolean success = parkingSpaceService.updateAvailability(id, false);
        return success ? ResponseEntity.ok("Marked as unavailable")
                : ResponseEntity.badRequest().body("Parking space not found");
    }
}
