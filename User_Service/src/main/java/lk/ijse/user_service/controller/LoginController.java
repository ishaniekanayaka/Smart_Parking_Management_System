package lk.ijse.user_service.controller;


import lk.ijse.user_service.Response.JWTAuthResponse;
import lk.ijse.user_service.Response.SignIn;
import lk.ijse.user_service.Response.SignUp;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import lk.ijse.user_service.repository.UserRepo;
import lk.ijse.user_service.service.AuthService;
import lk.ijse.user_service.service.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("api/Login/auth")
@CrossOrigin
public class LoginController {


    @Autowired
    JWTService jwtService;

    @Autowired
    private AuthService authenticationService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/signIn")
    public ResponseEntity<JWTAuthResponse> signIN(
            @RequestBody SignIn signIn,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("Login Request with Authorization header: " + authHeader);

        // 1. Perform authentication
        JWTAuthResponse response = authenticationService.signIn(signIn);

        // 2. Activate user after successful login
        Optional<User> userOptional = userRepo.findByEmail(signIn.getEmail()); // or findByUsername()

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isActive()) {
                user.setActive(true); // set isActive = true
                userRepo.save(user);
                System.out.println("User activated after login.");
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> saveUser(@RequestBody SignUp signUp) {
        return ResponseEntity.ok(authenticationService.Register(signUp));
    }


    /* @PostMapping(value = "/signUp",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> saveUser(@RequestPart ("firstName") String firstName,
                                                    @RequestPart("lastName") String lastName,
                                                    @RequestPart("phoneNumber") String phoneNumber,
                                                    @RequestPart("role") String role,
                                                    @RequestPart ("email") String email,
                                                    @RequestPart ("password") String password

                                                    ){

        SignUp signUp = new SignUp();

        signUp.setFirstName(firstName);
        signUp.setRole(role);
        signUp.setLastName(lastName);
        signUp.setPhoneNumber(phoneNumber);
        signUp.setEmail(email);
        signUp.setPassword(password);

        return ResponseEntity.ok(authenticationService.Register(signUp));
    }
*/
    @PreAuthorize("hasAnyRole('ADMIN')")
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




