package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementsRepository extends JpaRepository<Element, Long> {

    List<Element> findByPageOrderByIdAsc(Page page);

}
