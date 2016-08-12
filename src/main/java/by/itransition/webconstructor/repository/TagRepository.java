package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByValueIgnoringCase(String value);

}
