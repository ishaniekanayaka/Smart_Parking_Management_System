package lk.ijse.user_service.service.impl;

import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;
import lk.ijse.user_service.repository.UserRepo;
import lk.ijse.user_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    /*public UserDTO register(UserDTO dto, String password) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .password(password)
                .build();
        return toDTO(userRepo.save(user));
    }

    public UserDTO getUserByEmail(String email) {
        return toDTO(userRepo.findByEmail(email));
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
    }*/

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO saveUser(UserDTO dto) {
        User user = mapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // encode here
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        return mapper.map(userRepository.save(user), UserDTO.class);
    }
    /*@Override
    public UserDTO saveUser(UserDTO dto) {
        User user = mapper.map(dto, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        return mapper.map(userRepository.save(user), UserDTO.class);
    }*/

    @Override
    public UserDTO updateUser(UserDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
       /* user.setPassword(dto.getPassword());*/
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType());
        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDTO.class);
    }
}
