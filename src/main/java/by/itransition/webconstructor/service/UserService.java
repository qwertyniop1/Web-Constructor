package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.domain.VerificationToken;
import by.itransition.webconstructor.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    User getUser(String username);

    List<UserDto> getAllUsers();

    boolean editUser(String username, UserDto user);

    boolean updateUser(User user, UserDto profile);

    void changeAvatar(User user, String photo);

    boolean registerUser(UserDto userDto, HttpServletRequest request);

    VerificationToken createVerificationToken(User/*String*/ user, String token);

    boolean activateUser(String token);

    void makeAdmin(String username);

    void makeUser(String username);

    void banUser(String username);

    void unbanUser(String username);

    void deleteUser(String username);

    void resendConfirmationMessage(String email);
}

