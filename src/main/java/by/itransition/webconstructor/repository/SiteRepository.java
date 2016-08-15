package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findByUser(User user);

    Site findByUserAndNameAllIgnoringCase(User user, String name);

}
