package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    User findByEmail(String email);

}
