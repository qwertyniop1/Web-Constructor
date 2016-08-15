package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.*;
import by.itransition.webconstructor.dto.SiteDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.repository.*;
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
    private static final String DEFAULT_SITE_NAME = "New site";

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private TagRepository tagRepository;

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
        Site site = siteRepository.findByUserAndNameAllIgnoringCase(user, name); //TODO only 1 site
        if (site == null) {
            throw new ResourceNotFoundException();
        }
        return site;
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
        site.setName(generateName(siteRepository.findByUser(user)));
        site.setUser(user);
        site.setLogo(DEFAULT_LOGO);
        site.setDescription("");
        site.setMenuOrientation(MenuOrientation.NONE);
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
