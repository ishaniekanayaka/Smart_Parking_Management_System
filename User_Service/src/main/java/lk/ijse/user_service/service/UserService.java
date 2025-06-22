package lk.ijse.user_service.service;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;

import java.util.List;

public interface UserService {
    /*UserDTO register(UserDTO dto, String password);
    public UserDTO getUserByEmail(String email);
    UserDTO toDTO(User user);*/

    UserDTO saveUser(UserDTO dto);
    UserDTO updateUser(UserDTO dto);
    List<UserDTO> getAllActiveUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
}
