package lk.ijse.user_service.Response;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignIn {
    @Email
    private String email;
    @NotNull(message = "password cannot be null")
    private String password;
}
