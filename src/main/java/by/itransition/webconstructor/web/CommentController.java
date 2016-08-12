package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/comments")
@RestController
public class CommentController {

    private static final String STATUS_OK = "STATUS_OK";

    @Autowired
    CommentService commentService;

    @GetMapping("/list.json")
    public List<CommentDto> index(@RequestParam Long page) {
        User user = null;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ignored) {

        }
        return commentService.getComments(page, user);
    }

    @PostMapping("/add")
    public String add(@RequestBody CommentDto comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.create(user, comment);
        return STATUS_OK;
    }

    @PostMapping("/edit")
    public String edit(@RequestBody CommentDto comment) {
        commentService.update(comment);
        return STATUS_OK;
    }

    @PostMapping("/remove")
    public String remove(@RequestBody CommentDto comment) {
        commentService.remove(comment);
        return STATUS_OK;
    }

    @PostMapping("/like/add")
    public String addLike(@RequestBody CommentDto comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.addLike(user, comment);
        return STATUS_OK;
    }

    @PostMapping("/like/remove")
    public String removeLike(@RequestBody CommentDto comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.removeLike(user, comment);
        return STATUS_OK;
    }

}
