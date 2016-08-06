package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;

import java.util.List;

public interface SiteService {

    Site getSite(Long id);

    List<Site> getSites(User user);

//    List<Page> getPages(Long siteId);

    long create(User user);

    void update(Long id, String name);

    void delete(Long id);
}
