package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.dto.PageDto;

public interface PageService {

    Page getPage(Long id);

//    List<Element> getElements(Long id);

    long create(Long siteId);

    void update(Long id, PageDto page);

    void delete(Long id);

}
