package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;

import java.util.List;

public interface CommentService {

    void create(User user, CommentDto comment);

    void update(CommentDto comment);

    void remove(CommentDto comment);

    List<CommentDto> getComments(Long id, User user);

    void addLike(User user, CommentDto comment);

    void removeLike(User user, CommentDto comment);

}
