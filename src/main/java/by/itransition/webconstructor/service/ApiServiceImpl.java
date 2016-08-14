package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApiServiceImpl implements ApiService{

    private static final int MAX_TAGS_QUANTITY = 20;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTopTags() {
        List<Tag> top = tagRepository.findAll();
        top.sort((o1, o2) -> o2.getSites().size() - o1.getSites().size());
        return top.size() > MAX_TAGS_QUANTITY ? new ArrayList<>(top.subList(0, MAX_TAGS_QUANTITY - 1)) : top;
    }
}
