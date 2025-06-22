package lk.ijse.user_service.controller;

import jakarta.validation.Valid;
import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO) {
        userService.saveUser(userDTO);
        return ResponseEntity.ok("User saved successfully");
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody @Valid  UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @GetMapping
    public List<UserDTO> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}
