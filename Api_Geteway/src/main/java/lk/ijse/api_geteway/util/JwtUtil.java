package lk.ijse.api_geteway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Map<String, String> extractUserInfo(String token) throws RuntimeException {
        try {
            Claims claims = extractAllClaims(token);

            if (claims.getExpiration().before(new Date())) {
                throw new RuntimeException("Token expired");
            }

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            if (email == null || email.isEmpty()) {
                throw new RuntimeException("Missing username claim");
            }
            if (role == null || role.isEmpty()) {
                throw new RuntimeException("Missing role claim");
            }

            // map ekkata data data return karanawa
            // data kipayak ekapara geniyanna ona nisa
            return Map.of("email", email, "role", role);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

}