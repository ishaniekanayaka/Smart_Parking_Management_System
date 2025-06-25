package lk.ijse.user_service.controller;


import lk.ijse.user_service.dto.RoleUpdateRequestDTO;
import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import lk.ijse.user_service.repository.UserRepo;
import lk.ijse.user_service.service.JWTService;
import lk.ijse.user_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService;




    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        System.out.println("GET POST");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Token");
        }

        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        // token is valid → continue to fetch user data
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    @GetMapping("getUserId/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Optional<User> userOpt = userRepo.findById(id);

        if (userOpt.isPresent()) {
            UserDTO dto = modelMapper.map(userOpt.get(), UserDTO.class);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping(value = "update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable ("id") Long id,
                                           @RequestBody UserDTO userDto) {
        try {

            boolean save = userService.userUpdate(id, userDto);

            if (save) {
                new ResponseEntity<Void>(HttpStatus.OK);
            }
            new ResponseEntity<Void>(HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

  /*  @PreAuthorize("hasAnyRole('ADMIN')")*/
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable ("id") Long id){

        boolean save = userService.userDelete(id);

        if (save){
            new ResponseEntity<Void>(HttpStatus.OK);
        }else {
            new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

   /* @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("updateRole/{id}")
    public ResponseEntity<Void> updateRole (@PathVariable ("id") Long  id){

        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setRole("ADMIN");
            userRepo.save(user);
        }else {
            new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }*/


   @PutMapping("updateRole/{id}")
   public ResponseEntity<Void> updateRole(@PathVariable Long id,
                                          @RequestBody RoleUpdateRequestDTO request) {
       Optional<User> optionalUser = userRepo.findById(id);
       if (optionalUser.isPresent()) {
           User user = optionalUser.get();
           user.setRole(request.getRole());  // JSON body එකෙන් role එක ගන්නවා
           userRepo.save(user);
           return new ResponseEntity<>(HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getVehicleById(@PathVariable Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            UserDTO userDto = modelMapper.map(user.get(), UserDTO.class);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
