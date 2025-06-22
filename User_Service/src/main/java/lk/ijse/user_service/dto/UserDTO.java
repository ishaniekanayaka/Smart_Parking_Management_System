package lk.ijse.user_service.dto;

import lk.ijse.user_service.entity.util.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
   /* private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;*/
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserType userType;
    private LocalDateTime createdAt;
    private boolean active;
}
