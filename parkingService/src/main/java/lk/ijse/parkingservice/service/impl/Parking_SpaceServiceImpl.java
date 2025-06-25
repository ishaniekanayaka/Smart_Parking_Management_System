package lk.ijse.parkingservice.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.parkingservice.dto.Parking_spaceDTO;
import lk.ijse.parkingservice.dto.UserDto;
import lk.ijse.parkingservice.entity.Parking_Space;
import lk.ijse.parkingservice.repo.ParkingRepo;
import lk.ijse.parkingservice.service.Parking_spaceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Parking_SpaceServiceImpl implements Parking_spaceService {

    private static final String USER_SERVICE_URL = "http://localhost:8080/user-service/api/v1/user/";

    @Autowired
    private ParkingRepo parkingRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean saveParkingSpace(Parking_spaceDTO dto, String authHeader) {
        if (dto.getUserId() == null) {
            System.out.println("User ID is null");
            return false;
        }

        if (!isUserActive(dto.getUserId(), authHeader)) {
            return false;
        }

        Parking_Space space = modelMapper.map(dto, Parking_Space.class);
        parkingRepo.save(space);
        return true;
    }

    @Override
    public boolean updateSpace(Parking_spaceDTO dto, Long id, String authHeader) {
        Optional<Parking_Space> optional = parkingRepo.findById(id);
        if (optional.isEmpty()) {
            System.out.println("Parking space not found with ID: " + id);
            return false;
        }

        if (!isUserActive(dto.getUserId(), authHeader)) {
            return false;
        }

        Parking_Space space = optional.get();
        space.setAvailable(dto.isAvailable());
        space.setLocation(dto.getLocation());
        space.setZone(dto.getZone());
        space.setUserId(dto.getUserId());

        parkingRepo.save(space);
        return true;
    }

    @Override
    public boolean deleteSpace(Long id, String authHeader) {
        if (parkingRepo.existsById(id)) {
            parkingRepo.deleteById(id);
            System.out.println("Parking space deleted successfully.");
            return true;
        }
        System.out.println("Parking space not found.");
        return false;
    }

    @Override
    public List<Parking_spaceDTO> getAllSpaces() {
        List<Parking_Space> entityList = parkingRepo.findAll();
        return modelMapper.map(entityList, new TypeToken<List<Parking_spaceDTO>>() {}.getType());
    }

    @Override
    public Parking_spaceDTO getSpaceById(Long id) {
        Optional<Parking_Space> optional = parkingRepo.findById(id);
        return optional.map(space -> modelMapper.map(space, Parking_spaceDTO.class)).orElse(null);
    }

    @Override
    public boolean updateAvailability(Long id, boolean available) {
        Optional<Parking_Space> optional = parkingRepo.findById(id);
        if (optional.isPresent()) {
            Parking_Space space = optional.get();
            space.setAvailable(available);
            parkingRepo.save(space);
            return true;
        }
        return false;
    }

    // Utility method to call user service
    private boolean isUserActive(Long userId, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDto> response = restTemplate.exchange(
                    USER_SERVICE_URL + userId,
                    HttpMethod.GET,
                    entity,
                    UserDto.class
            );

            return response.getStatusCode().is2xxSuccessful() &&
                    response.getBody() != null &&
                    response.getBody().isActive();
        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return false;
        }
    }
}
