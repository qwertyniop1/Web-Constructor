package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.dto.PageDto;

import java.util.List;

public interface PageService {

    Page getPage(Long id);

    void addComment(User user, CommentDto comment);

    void updateComment(CommentDto comment);

    void deleteComment(CommentDto comment);

    List<CommentDto> getComments(Long id, User user);

    long create(Long siteId);

    void update(Long id, PageDto page);

    void delete(Long id);

}
