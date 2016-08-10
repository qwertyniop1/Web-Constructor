package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.*;
import by.itransition.webconstructor.dto.SiteDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.repository.PageRepository;
import by.itransition.webconstructor.repository.RateRepository;
import by.itransition.webconstructor.repository.SiteRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final String DEFAULT_LOGO = "rwkhctdn9wyli2cvwfxn";

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private RateRepository rateRepository;

    @Override
    public Site getSite(Long id) {
        Site site = siteRepository.findOne(id);
        if (site == null) {
            throw new ResourceNotFoundException();
        }
        return site;
    }

    @Override
    public Site getSite(User user, String name) {
        List<Site> sites = siteRepository.findByUserAndName(user, name); //TODO only 1 site
        if (sites.size() == 0) {
            throw new ResourceNotFoundException();
        }
        return sites.get(0);
    }

    @Override
    public List<Site> getSites(User user) {
        return siteRepository.findByUser(user);
    }

    @Override
    public Map<Long, Double> getSitesRates(List<Site> sites) {
        Map<Long, Double> ratings = new HashMap<>();
        for (Site site : sites) {
            ratings.put(site.getId(), average(site.getRates()));
        }
        return ratings;
    }

    @Override
    public void setRate(Long id, double rate, User user) {
        Site site = siteRepository.findOne(id);
        Rate siteRate = rateRepository.findByUserAndSite(user, site);
        if (siteRate == null) {
            siteRate = new Rate();
            siteRate.setUser(user);
            siteRate.setSite(site);
        }
        siteRate.setValue(rate);
        rateRepository.save(siteRate);
    }

    @Override
    public double getRate(Long id, User user) {
        Site site = siteRepository.findOne(id);
        Rate siteRate = rateRepository.findByUserAndSite(user, site);
        return siteRate != null ? siteRate.getValue() : 0;
    }

//    @Override
//    public List<Page> getPages(Long siteId) {
//        return pageRepository.findBySite(siteRepository.findOne(siteId));
//    }

    @Override
    public long create(User user) {
        Site site = new Site();
        site.setUser(user);
        site.setLogo(DEFAULT_LOGO);
        site.setMenuOrientation(MenuOrientation.NONE);
        return siteRepository.save(site).getId();
    }

    @Override
    public void update(Long id, SiteDto siteDto) {
        Site site = siteRepository.findOne(id);
        site.setName(siteDto.getName());
        String logo = siteDto.getLogo();
        logo = (logo == null || Objects.equals(logo, "")) ? DEFAULT_LOGO : logo;
        site.setLogo(logo);
        site.setDescription(siteDto.getDescription());
        site.setMenuOrientation(getMenuOrientation(siteDto));
        siteRepository.save(site);
    }

    @Override
    public void delete(Long id) {
        siteRepository.delete(id);
    }

    private MenuOrientation getMenuOrientation(SiteDto site) {
        List<String> menus = site.getMenus();
        if (menus.size() == 0) {
            return MenuOrientation.NONE;
        }
        if (menus.size() == 2) {
            return MenuOrientation.BOTH;
        }
        return MenuOrientation.valueOf(menus.get(0).toUpperCase());
    }

    private double average(Set<Rate> rates) {
        double result = 0;
        for (Rate rate : rates) {
            result += rate.getValue();
        }
        return result != 0 ? Math.round(result / rates.size() * 2) / 2.0 : 0;
    }
}
