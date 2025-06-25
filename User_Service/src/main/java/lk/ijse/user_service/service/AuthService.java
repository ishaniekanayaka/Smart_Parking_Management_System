package lk.ijse.user_service.service;


import lk.ijse.user_service.Response.JWTAuthResponse;
import lk.ijse.user_service.Response.SignIn;
import lk.ijse.user_service.Response.SignUp;
import lk.ijse.user_service.dto.AuthDTO;


public interface AuthService {
    JWTAuthResponse Register(SignUp signUp);
    JWTAuthResponse signIn(SignIn signIn);
    JWTAuthResponse refreshToken(String refreshToken);
    String validateToken(String token);


}
