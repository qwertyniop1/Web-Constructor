package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.dto.UserDto;

import java.util.List;

public interface ApiService {

    List<Tag> getAllTags();

    List<Tag> getTopTags();

    List<UserDto> getTopUsers(Integer quantity);

}
