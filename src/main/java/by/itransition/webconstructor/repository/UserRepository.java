package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, String> {

    User findByUsername(String username);

}
