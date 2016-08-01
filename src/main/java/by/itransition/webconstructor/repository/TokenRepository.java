package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
