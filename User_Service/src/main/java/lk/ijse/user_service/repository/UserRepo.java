package lk.ijse.user_service.repository;

import lk.ijse.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    /*User findByEmail(String email);*/
    Optional<User> findByEmail(String email);
}
