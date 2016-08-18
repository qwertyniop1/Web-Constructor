package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.SiteDto;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

public interface SiteService {

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.user.username == principal.username")
    Site getSite(Long id);

    Site getSite(User user, String name);

    List<Site> getAllSites();

    List<Site> getTopSites(Integer quantity);

    List<Site> getSites(User user);

    List<Site> findByTag(String tag);

    Map<Long, Double> getSitesRates(List<Site> sites);

    void setRate(Long id, double rate, User user);

    double getRate(Long id, User user);

//    List<Page> getPages(Long siteId);

    long create(User user);

    boolean update(Long id, SiteDto site);

    void delete(Long id);
}
