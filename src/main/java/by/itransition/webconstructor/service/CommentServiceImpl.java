package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Comment;
import by.itransition.webconstructor.domain.Like;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.event.OnCommentAddEvent;
import by.itransition.webconstructor.event.OnLikeAddEvent;
import by.itransition.webconstructor.repository.CommentRepository;
import by.itransition.webconstructor.repository.LikeRepository;
import by.itransition.webconstructor.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void create(User user, CommentDto comment) {
        Page page = pageRepository.findOne(comment.getPage());
        Comment newComment = new Comment();
        newComment.setUser(user);
        newComment.setPage(page);
        newComment.setContent(comment.getContent());
        newComment.setInternalId(comment.getId());
        newComment.setParent(comment.getParent());
        eventPublisher.publishEvent(new OnCommentAddEvent(user, commentRepository.findByUser(user).size() + 1)); //FIXME lol bug LAZY
        commentRepository.save(newComment);
    }

    @Override
    public void update(CommentDto comment) {
        Page page = pageRepository.findOne(comment.getPage());
        Comment newComment = commentRepository.findByPageAndInternalId(page, comment.getId());
        if (newComment == null) {
            return;
        }
        newComment.setPage(page);
        newComment.setContent(comment.getContent());
        newComment.setInternalId(comment.getId());
        newComment.setParent(comment.getParent());
        commentRepository.save(newComment);
    }

    @Override
    public void remove(CommentDto comment) {
        Comment deletedComment = commentRepository
                .findByPageAndInternalId(pageRepository.findOne(comment.getPage()), comment.getId());
        commentRepository.delete(deletedComment);
    }

    @Override
    public List<CommentDto> getComments(Long id, User user) {
        List<Comment> comments = commentRepository.findByPage(pageRepository.findOne(id));
        List<CommentDto> resultList = new ArrayList<>();
        for (Comment comment : comments) {
            resultList.add(new CommentDto(comment, user));
        }
        return resultList;
    }

    @Override
    public void addLike(User user, CommentDto comment) {
        eventPublisher.publishEvent(new OnLikeAddEvent(user, likeRepository.findByUser(user).size() + 1)); //FIXME lol bug LAZY
        likeRepository.save(new Like(user, commentRepository
                .findByPageAndInternalId(pageRepository.findOne(comment.getPage()), comment.getId())));
    }

    @Override
    public void removeLike(User user, CommentDto comment) {
        likeRepository.delete(likeRepository
                .findByUserAndComment(user, commentRepository
                .findByPageAndInternalId(pageRepository
                        .findOne(comment.getPage()), comment.getId())));
    }
}
