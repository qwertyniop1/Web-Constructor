package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Tag;

import java.util.List;

public interface ApiService {

    List<Tag> getAllTags();

    List<Tag> getTopTags();

}
