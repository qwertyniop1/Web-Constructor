package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Site;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SiteRepository extends Repository<Site, Long> {

    List<Site> findByUser(String user);

}
