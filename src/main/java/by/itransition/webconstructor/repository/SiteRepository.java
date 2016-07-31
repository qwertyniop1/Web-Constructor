package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Site;
import org.springframework.data.repository.Repository;

public interface SiteRepository extends Repository<Site, Long> {

    Site findByUser(String user);

}
