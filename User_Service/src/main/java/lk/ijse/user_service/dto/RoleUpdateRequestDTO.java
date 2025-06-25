package lk.ijse.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleUpdateRequestDTO {
    private String role;

    // getters and setters
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
