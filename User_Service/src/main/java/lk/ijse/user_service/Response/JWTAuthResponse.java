package lk.ijse.user_service.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthResponse {
    private String tokens;
    private String role;
    private String name;
    private int userId;
    @Column(columnDefinition = "LONGTEXT")
    private String proPic;


}
