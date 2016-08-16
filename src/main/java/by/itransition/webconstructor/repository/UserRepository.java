package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByEnabled(boolean enabled);

    User findByUsername(String username);

    User findByEmail(String email);

}
