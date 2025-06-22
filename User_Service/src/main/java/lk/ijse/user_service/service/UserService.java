package lk.ijse.user_service.service;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    int saveUser(UserDTO userDTO);

    UserDTO loadUserDetailsByUsername(String email);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    List<UserDTO> getAllUsers();

    boolean deactivateUser(Long userId);

    int updateUser(UserDTO userDTO);

}
