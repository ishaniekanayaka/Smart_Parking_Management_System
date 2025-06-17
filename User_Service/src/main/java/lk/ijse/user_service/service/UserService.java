package lk.ijse.user_service.service;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;

public interface UserService {
    UserDTO register(UserDTO dto, String password);
    public UserDTO getUserByEmail(String email);
    UserDTO toDTO(User user);
}
