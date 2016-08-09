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
import java.util.Calendar;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private static final String DEFAULT_AVATAR = "anonymous-man_bszhtk";
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
    public void updateUser(User user, UserDto profile) {
        user.setFirstname(profile.getFirstname());
        user.setLastname(profile.getLastname());
        userRepository.save(user);
    }

    @Override
    public boolean registerUser(UserDto userDto, HttpServletRequest request) {
        User user = saveUser(userDto);
        if (user == null) {
            return false;
        }
        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                    String.format("%s://%s:%d", request.getScheme(),
                            request.getServerName(), request.getServerPort()), request.getLocale()));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public VerificationToken createVerificationToken(User user, String token) {
        return tokenRepository.save(new VerificationToken(user, token));
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
    public void resendConfirmationMessage(String email) {

    }

    private User saveUser(UserDto userDto) {
        User user = createUser(userDto);
        return (!userExist(userDto) && userRepository.save(user) != null) ? user : null;
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
