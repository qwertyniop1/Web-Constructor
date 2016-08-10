package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.SiteDto;

import java.util.List;

public interface SiteService {

    Site getSite(Long id);

    Site getSite(User user, String name);

    List<Site> getSites(User user);

    void setRate(Long id, double rate, User user);

    double getRate(Long id, User user);

//    List<Page> getPages(Long siteId);

    long create(User user);

    void update(Long id, SiteDto site);

    void delete(Long id);
}
