package lk.ijse.user_service.controller;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterRequest request) {
        return userService.register(request.getUser(), request.getPassword());
    }

    @GetMapping("/email/{email}")
    public UserDTO getUser(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}

// Request wrapper for registration
class UserRegisterRequest {
    private UserDTO user;
    private String password;

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
