package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.error.UserAlreadyExistException;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(UserDto user) {
        return !userExist(user) && userRepository.save(createUser(user)) != null;
    }

    private User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true); // TODO qqqq
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
