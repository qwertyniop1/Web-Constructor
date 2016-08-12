package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Comment;
import by.itransition.webconstructor.domain.Like;
import by.itransition.webconstructor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserAndComment(User user, Comment comment);

}
