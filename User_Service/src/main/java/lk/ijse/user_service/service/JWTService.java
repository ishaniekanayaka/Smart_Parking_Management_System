package lk.ijse.user_service.service;

import lk.ijse.user_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;


public interface JWTService {
    String extractUserName(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    //String generateToken(UserDetails userDetails);
    String refreshToken(UserDetails userDetails);
    boolean validateToken(String token);
    String generateToken(User user);
    String generateToken(Map<String, Object> claims, String subject);
}
