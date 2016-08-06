package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;

import java.util.List;

public interface PageService {

    Page getPage(Long id);

    long create(Long siteId);

    void delete(Long id);

}
