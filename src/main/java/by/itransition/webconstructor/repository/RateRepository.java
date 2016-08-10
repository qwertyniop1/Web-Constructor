package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Rate;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate findByUserAndSite(User user, Site site);

}
