package lk.ijse.vehicleservice.service.Impl;


import jakarta.transaction.Transactional;
import lk.ijse.vehicleservice.dto.UserDto;
import lk.ijse.vehicleservice.dto.vehicleDto;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.entity.util.ParkingStatus;
import lk.ijse.vehicleservice.repo.VehicleRepo;
import lk.ijse.vehicleservice.service.VehicleServive;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleServive {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private RestTemplate restTemplate;


    public boolean registerVehicle(vehicleDto vehicleDto, String authHeader) {

        Optional<Vehicle> existing = vehicleRepo.findByPlateNumber(vehicleDto.getPlateNumber());
        if (existing.isPresent()) {
            return false;
        }


        String url = "http://localhost:8080/user-service/api/v1/user/" + vehicleDto.getUserId();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                UserDto userDto = response.getBody();

                if (userDto != null && userDto.isActive()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setPlateNumber(vehicleDto.getPlateNumber());
                    vehicle.setBrand(vehicleDto.getBrand());
                    vehicle.setColor(vehicleDto.getColor());
                    vehicle.setType(vehicleDto.getType());
                    vehicle.setStatus(ParkingStatus.NOT_PARKED);
                    vehicle.setUserId(vehicleDto.getUserId());

                    vehicleRepo.save(vehicle);
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
    public boolean updateVehicle(vehicleDto vehicleDto, Long id,String authHeader) {


        Optional<Vehicle> existingOpt = vehicleRepo.findByPlateNumber(vehicleDto.getPlateNumber());
        if (existingOpt.isEmpty()) {
            System.out.println("not found vehicle plate Number: " + vehicleDto.getPlateNumber());
            return false;
        }

        Vehicle vehicle = existingOpt.get();

        String url = "http://localhost:8080/user-service/api/v1/user/" + vehicleDto.getUserId();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", String.valueOf(authHeader));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                UserDto userDto = response.getBody();

                if (userDto != null && userDto.isActive()) {
                    vehicle.setBrand(vehicleDto.getBrand());
                    vehicle.setColor(vehicleDto.getColor());
                    vehicle.setType(vehicleDto.getType());
                    vehicle.setStatus(ParkingStatus.NOT_PARKED);
                    vehicle.setUserId(vehicleDto.getUserId());

                    vehicleRepo.save(vehicle);

                    System.out.println("Vehicle updated successfully.");
                    return true;
                } else {
                    System.out.println("User is inactive or not found.");
                    return false;
                }
            } else {
                System.out.println("Failed to contact user-service. Status: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteVehicle(Long id, String authHeader) {
        Optional<Vehicle> optionalUser = vehicleRepo.findById(id);

        if (optionalUser.isPresent()){
            vehicleRepo.deleteById(id);
            System.out.println("Vehicle delete successfully.");
            return true;
        }else {
            System.out.println("Vehicle delete Failed.");
            return false;
        }

    }


   /* @Override
    public List<Vehicle> getAllVehicles() {
            return modelMapper.map(vehicleRepo.findAll(), new TypeToken<List<vehicleDto>>(){}.getType());
    }*/
   @Override
   public List<vehicleDto> getAllVehicles(String authHeader) {
       List<Vehicle> vehicles = vehicleRepo.findAll();
       List<vehicleDto> dtoList = new ArrayList<>();

       for (Vehicle v : vehicles) {
           vehicleDto dto = modelMapper.map(v, vehicleDto.class);

           try {
               String url = "http://localhost:8080/user-service/api/v1/user/" + v.getUserId();

               HttpHeaders headers = new HttpHeaders();
               headers.set("Authorization", authHeader); // ✅ Use actual token
               HttpEntity<Void> entity = new HttpEntity<>(headers);

               ResponseEntity<UserDto> response = restTemplate.exchange(
                       url,
                       HttpMethod.GET,
                       entity,
                       UserDto.class
               );

               if (response.getStatusCode().is2xxSuccessful()) {
                   UserDto userDto = response.getBody();
                   if (userDto != null) {
                       dto.setUserName(userDto.getFirstName() + " " + userDto.getLastName());
                       dto.setPhoneNumber(userDto.getPhoneNumber());
                   }
               }
           } catch (Exception e) {
               System.err.println("Error fetching user data for userId=" + v.getUserId());
               e.printStackTrace();
           }

           dtoList.add(dto);
       }

       return dtoList;
   }




}



