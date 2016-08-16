package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.dto.PageDto;

import java.util.List;

public interface PageService {

    Page getPage(Long id);

    Page getUserPage(Long id, String username, String site);

    List<Element> getElements(Long id);

    long create(Long siteId);

    void update(Long id, PageDto page);

    void delete(Long id);

}
