package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findBySite(Site site);

}
