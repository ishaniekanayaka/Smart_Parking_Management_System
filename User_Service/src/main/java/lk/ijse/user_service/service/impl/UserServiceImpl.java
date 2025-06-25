package lk.ijse.user_service.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.entity.User;

import lk.ijse.user_service.exception.NotFoundException;
import lk.ijse.user_service.repository.UserRepo;
import lk.ijse.user_service.service.UserService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean userUpdate(Long id, UserDTO userDto) {

        User user = userRepo.findById(id).orElse(null);

        if(user != null){
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setRole(userDto.getRole());
            user.setPassword(user.getPassword());

            userRepo.save(user);
            return  true;
        }else {
            return false;
        }
    }

    @Override
    public boolean userDelete(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()){
            userRepo.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return modelMapper.map(userRepo.findAll(), new TypeToken<List<UserDTO>>(){}.getType());

    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepo.findByEmail(username).orElseThrow(
                    ()-> new NotFoundException("User Name Not Found")
            );

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
            );
        };
    }
}
