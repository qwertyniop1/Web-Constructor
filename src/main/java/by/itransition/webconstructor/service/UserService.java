package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.UserDto;

public interface UserService {

    boolean registerUser(UserDto userDto);

}
