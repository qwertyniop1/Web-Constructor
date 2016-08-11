package by.itransition.webconstructor.dto;

import by.itransition.webconstructor.domain.Comment;
import by.itransition.webconstructor.domain.Role;
import by.itransition.webconstructor.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class CommentDto {

    private static final String PROFILE_URL_PREFIX = "/user/";
    private static final String PROFILE_PICTURE_URL_PREFIX = "http://res.cloudinary.com/itraphotocloud/image/" +
            "upload/w_400,h_400,c_crop,g_face,r_max/w_200/";
    private static final String PROFILE_PICTURE_URL_SUFFIX = ".png";

    private String id;

    private String parent;

    private String created;

    private String modified;

    private String content;

    private String fullname;

    private String profileURL;

    private String profilePictureURL;

    private boolean createdByAdmin;

    private boolean createdByCurrentUser;

    private int upvoteCount;

    private boolean userHasUpvoted;

    private Long page;

    public CommentDto(Comment comment, User currentUser) {
        this.id = comment.getInternalId();
        this.parent = comment.getParent();
        this.created = formatData(comment.getCreated());
        this.modified = formatData(comment.getModified());
        this.content = comment.getContent();
        getUserInfo(comment.getUser(), currentUser);
        this.upvoteCount = 0;
        this.userHasUpvoted = false; //FIXME lol
    }

    private String formatData(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
    }

    private void getUserInfo(User user, User currentUser) {
        this.fullname = createFullname(user);
        this.profileURL = creteProfileUrl(user);
        this.profilePictureURL = createProfilePictureUrl(user);
        this.createdByAdmin = isAdminComment(user);
        this.createdByCurrentUser = isCurrentUserComment(user, currentUser);
    }

    private boolean isCurrentUserComment(User user, User currentUser) {
        return user.getUsername().equals(currentUser.getUsername());
    }

    private boolean isAdminComment(User user) {
        return user.getRole().equals(Role.ROLE_ADMIN);
    }

    private String createProfilePictureUrl(User user) {
        return PROFILE_PICTURE_URL_PREFIX + user.getAvatar() + PROFILE_PICTURE_URL_SUFFIX;
    }

    private String creteProfileUrl(User user) {
        return PROFILE_URL_PREFIX + user.getUsername();
    }

    private String createFullname(User user) {
        return user.getFirstname() + " " + user.getLastname();
    }
}
