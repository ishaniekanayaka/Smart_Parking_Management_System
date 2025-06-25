package lk.ijse.user_service.service.impl;

import lk.ijse.user_service.Response.JWTAuthResponse;
import lk.ijse.user_service.Response.SignIn;
import lk.ijse.user_service.Response.SignUp;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import lk.ijse.user_service.exception.NotFoundException;
import lk.ijse.user_service.repository.UserRepo;
import lk.ijse.user_service.service.AuthService;
import lk.ijse.user_service.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public static String userRoleget = "";
    public static int userIDget = 0;
    public static String  UserNameGet = "";



    @Override
    public JWTAuthResponse Register(SignUp signUp) {
        UserDTO userDTO = UserDTO.builder()
                .email(signUp.getEmail())
                .firstName(signUp.getFirstName())
                .lastName(signUp.getLastName())
                .role(signUp.getRole())

                .phoneNumber(signUp.getPhoneNumber())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .build();

        User user1 = modelMapper.map(userDTO, User.class);
        userRepo.save(user1);

        String generateToken = jwtService.generateToken(user1);

        return JWTAuthResponse.builder()
                .tokens(generateToken)
                .build();
    }


    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword())
        );

        User userEntity = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userEntity.getRole());
        claims.put("userId", userEntity.getId());
        claims.put("name", userEntity.getFirstName());
        claims.put("email" ,userEntity.getEmail());

        String generateToken = jwtService.generateToken(claims, userEntity.getEmail());
        System.out.println("==================================  :" + generateToken);

        userRoleget = String.valueOf(userEntity.getRole());
        userIDget = Math.toIntExact(userEntity.getId());


        return JWTAuthResponse.builder()
                .tokens(generateToken)
                .name(userEntity.getFirstName())
                .userId(userIDget)
                .role(userRoleget)
                .build();
    }

    @Override
    public JWTAuthResponse refreshToken(String refreshToken) {
        String user =jwtService.extractUserName(refreshToken);
        User findUser =userRepo.findByEmail(user).orElseThrow(()-> new NotFoundException("Couldn't find User"));
        String token =jwtService.refreshToken( findUser);
        return JWTAuthResponse.builder().tokens(token).build();
    }

    @Override
    public String validateToken(String token) {
        try {
            return jwtService.extractUserName(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
