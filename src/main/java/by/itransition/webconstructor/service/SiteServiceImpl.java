package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.*;
import by.itransition.webconstructor.dto.SiteDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.event.OnRateAddEvent;
import by.itransition.webconstructor.event.OnSiteCreateEvent;
import by.itransition.webconstructor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final String DEFAULT_LOGO = "rwkhctdn9wyli2cvwfxn";
    private static final String DEFAULT_SITE_NAME = "New site";
    private static final int DEFAULT_TOP_SIZE = 10;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
        Site site = siteRepository.findByUserAndNameAllIgnoringCase(user, name);
        if (site == null) {
            throw new ResourceNotFoundException();
        }
        return site;
    }

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    @Override
    public List<Site> getTopSites(Integer quantity) {
        if (quantity == null) {
            quantity = DEFAULT_TOP_SIZE;
        }
        List<Site> sites = siteRepository.findAll();
        sites.sort((o1, o2) -> {
            int res = Double.compare(average(o2.getRates()), average(o1.getRates()));
            return res != 0 ? res : o2.getRates().size() - o1.getRates().size();
        });
        return sites.size() > quantity ? new ArrayList<>(sites.subList(0, quantity)) : sites;
    }

    @Override
    public List<Site> getSites(User user) {
        return siteRepository.findByUser(user);
    }

    @Override
    public List<Site> findByTag(String value) {
        Tag tag = tagRepository.findByValueIgnoringCase(value);
        return tag != null ? new ArrayList<>(tag.getSites()) : null;
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
        if (rate == 0) {
            rateRepository.delete(siteRate);
            return;
        }
        siteRate.setValue(rate);
        eventPublisher.publishEvent(new OnRateAddEvent(user, rateRepository.findByUser(user).size() + 1));
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
        site.setName(generateName(siteRepository.findByUser(user)));
        site.setUser(user);
        site.setLogo(DEFAULT_LOGO);
        site.setDescription("");
        site.setMenuOrientation(MenuOrientation.NONE);
        site.setTheme(DesignTheme.FAIR);
        eventPublisher.publishEvent(new OnSiteCreateEvent(user, siteRepository.findByUser(user).size() + 1));
        return siteRepository.save(site).getId();
    }

    private String generateName(List<Site> sites) {
        String siteName = DEFAULT_SITE_NAME;
        int number = 1;
        while (!checkName(sites, siteName)) {
            siteName = DEFAULT_SITE_NAME + " " + number;
            number++;
        }
        return siteName;
    }

    private boolean checkName(List<Site> sites, String name) {
        for (Site site : sites) {
            if (site.getName().toLowerCase().equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean update(Long id, SiteDto siteDto) {
        Site site = siteRepository.findOne(id);
        if (!site.getName().equals(siteDto.getName()) && !checkName(siteDto.getName(), site.getUser())) {
            return false;
        }
        site.setName(siteDto.getName());
        site.setLogo(createLogo(siteDto));
        site.setDescription(siteDto.getDescription());
        site.setTheme(DesignTheme.valueOf(siteDto.getTheme().toUpperCase()));
        site.setMenuOrientation(getMenuOrientation(siteDto));
        addTags(site, siteDto.getTags());
        siteRepository.save(site);
        return true;
    }

    private boolean checkName(String name, User user) {
        return siteRepository.findByUserAndNameAllIgnoringCase(user, name) == null;
    }

    private void addTags(Site site, List<String> tags) {
        site.clearTags();
        for (String t : tags) {
            Tag tag = tagRepository.findByValueIgnoringCase(t);
            if (tag == null) {
                tag = new Tag(t);
            }
            tagRepository.save(tag);
            site.addTag(tag);
        }
    }

    private String createLogo(SiteDto siteDto) {
        String logo = siteDto.getLogo();
        return (logo == null || Objects.equals(logo, "")) ? DEFAULT_LOGO : logo;
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
