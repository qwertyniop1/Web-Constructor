package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Role;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.domain.VerificationToken;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.event.OnRegistrationCompleteEvent;
import by.itransition.webconstructor.repository.TokenRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private static final String DEFAULT_AVATAR = "anonymous-man_bszhtk";
    private static final int NAME_MIN_LENGHT = 2;
    private static final int NAME_MAX_LENGHT = 60;
    private static final int PASSWORD_MAX_LENGHT = 60;
    private static final int PASSWORD_MIN_LENGHT = 8;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findByEnabled(true).stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean editUser(String username, UserDto userDto) {
        User user = userRepository.findByUsername(username);
        if (!checkUser(user, userDto)) {
            return false;
        }
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        String password = userDto.getPassword();
        if (password.length() != 0) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return true;
//
//        user.setEnabled(false);
//        user.setRole(Role.ROLE_USER);
//        user.setAvatar(DEFAULT_AVATAR);
    }

    private boolean checkUser(User user, UserDto userDto) {
        User tmp = userRepository.findByEmail(userDto.getEmail());
        return !(tmp != null && !tmp.equals(user));
    }

    @Override
    public boolean updateUser(User user, UserDto profile) {
        if (!checkData(user, profile)) {
            return false;
        }
        updatePassword(user, profile);
        userRepository.save(user);
        return true;
    }

    private void updatePassword(User user, UserDto profile) {
        if (!isChangingPassword(profile)) {
            return;
        }
        if (!verifyPassword(user.getPassword(), profile.getOldPassword())) {
            return;
        }
        if (!passwordMathes(profile.getPassword(), profile.getMatchingPassword())) {
            return;
        }
        if (!validateNewPassword(profile.getPassword())) {
            return;
        }
        user.setPassword(passwordEncoder.encode(profile.getPassword()));
    }

    private boolean validateNewPassword(String password) {
        return password.length() >= PASSWORD_MIN_LENGHT && password.length() <= PASSWORD_MAX_LENGHT;
    }

    private boolean passwordMathes(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    private boolean verifyPassword(String userPassword, String password) {
        return passwordEncoder.matches(password, userPassword);
    }

    private boolean isChangingPassword(UserDto profile) {
        return profile.getOldPassword().length() != 0
                && profile.getPassword().length() != 0
                && profile.getMatchingPassword().length() != 0;
    }

    private boolean checkData(User user, UserDto profile) {
        if (!checkString(user, profile.getFirstname()) || !checkString(user, profile.getLastname())) {
            return false;
        }
        user.setFirstname(profile.getFirstname());
        user.setLastname(profile.getLastname());
        return true;
    }

    private boolean checkString(User user, String string) {
        return string.length() >= NAME_MIN_LENGHT && string.length() <= NAME_MAX_LENGHT;
    }

    @Override
    public boolean registerUser(UserDto userDto, HttpServletRequest request) {
        User user = saveUser(userDto);
        if (user == null) {
            return false;
        }
        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                    String.format("%s://%s", request.getScheme(),
                            request.getHeader("Host")), request.getLocale()));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public VerificationToken createVerificationToken(/*String*/User user, String token) {
        return tokenRepository.save(new VerificationToken(/*userRepository.findByUsername(user)*/user, token));
    }

    @Override
    public boolean activateUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return false;
        }
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public void makeAdmin(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return;
        }
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void banUser(String username) {
        changeUserLocked(username, true);
    }

    @Override
    public void unbanUser(String username) {
        changeUserLocked(username, false);
    }

    private void changeUserLocked(String username, boolean flag) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return;
        }
        user.setLocked(flag);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public void resendConfirmationMessage(String email) {

    }

    private User saveUser(UserDto userDto) {
        User user = createUser(userDto);
        return (!userExist(userDto) && userRepository.saveAndFlush(user) != null) ? user : null;
    }

    private User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(false);
        user.setRole(Role.ROLE_USER);
        user.setAvatar(DEFAULT_AVATAR);
        return user;
    }

    private boolean userExist(UserDto user) {
        return emailExist(user.getEmail()) || usernameExist(user.getUsername());
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
