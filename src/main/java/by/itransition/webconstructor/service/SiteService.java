package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;

import java.util.List;

public interface SiteService {

    List<Site> getSites(User user);

    List<Page> getPages(Long siteId);

}
