package lk.ijse.user_service.service;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    boolean userUpdate(Long id,UserDTO userDto);

    boolean userDelete(Long id);

    List<User> getAllUsers();

   /* boolean sendCodeToChangePassword(UserWithKey userWithKey);
*/
    UserDetailsService userDetailsService();
}
