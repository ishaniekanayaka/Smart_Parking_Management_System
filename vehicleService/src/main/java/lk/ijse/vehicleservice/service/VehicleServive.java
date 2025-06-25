package lk.ijse.vehicleservice.service;


import lk.ijse.vehicleservice.dto.vehicleDto;
import lk.ijse.vehicleservice.entity.Vehicle;

import java.util.List;

public interface VehicleServive {
     boolean registerVehicle(vehicleDto vehicleDto, String authHeader);
     boolean updateVehicle(vehicleDto vehicleDto,Long id,String authHeader);
     boolean deleteVehicle(Long id,String authHeader);
     /*List<Vehicle> getAllVehicles();*/
     List<vehicleDto> getAllVehicles(String authHeader);

}
