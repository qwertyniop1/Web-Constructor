package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.repository.TagRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiServiceImpl implements ApiService{

    private static final int MAX_TAGS_QUANTITY = 20;
    private static final int MAX_USERS_QUANTITY = 10;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTopTags() {
        List<Tag> top = tagRepository.findAll();
        top.sort((o1, o2) -> o2.getSites().size() - o1.getSites().size());
        return top.size() > MAX_TAGS_QUANTITY ? new ArrayList<>(top.subList(0, MAX_TAGS_QUANTITY)) : top;
    }

    @Override
    public List<UserDto> getTopUsers(Integer quantity) {
        if (quantity == null) {
            quantity = MAX_USERS_QUANTITY;
        }
        List<User> users = userRepository.findByEnabledAndLocked(true, false);
        users.sort((o1, o2) -> o2.getSites().size() - o1.getSites().size());
        if (users.size() > MAX_USERS_QUANTITY) {
            users = new ArrayList<>(users.subList(0, MAX_USERS_QUANTITY));
        }
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

}
