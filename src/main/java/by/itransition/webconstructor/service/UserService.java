package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.domain.VerificationToken;
import by.itransition.webconstructor.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    User getUser(String username);

    List<UserDto> getAllUsers();

    boolean updateUser(User user, UserDto profile);

    boolean registerUser(UserDto userDto, HttpServletRequest request);

    VerificationToken createVerificationToken(User user, String token);

    boolean activateUser(String token);

    void resendConfirmationMessage(String email);

}
