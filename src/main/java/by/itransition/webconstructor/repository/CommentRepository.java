package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Comment;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPage(Page page);

    Comment findByPageAndInternalId(Page page, String pageId);

    List<Comment> findByUser(User user);
}
