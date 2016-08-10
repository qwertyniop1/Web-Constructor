package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.SiteDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.repository.PageRepository;
import by.itransition.webconstructor.repository.SiteRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final String DEFAULT_LOGO = "rwkhctdn9wyli2cvwfxn";
    @Autowired
    private SiteRepository siteRepository;

//    @Autowired
//    private PageRepository pageRepository;


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

//    @Override
//    public List<Page> getPages(Long siteId) {
//        return pageRepository.findBySite(siteRepository.findOne(siteId));
//    }

    @Override
    public long create(User user) {
        Site site = new Site();
        site.setUser(user);
        site.setLogo(DEFAULT_LOGO);
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
        siteRepository.save(site);
    }

    @Override
    public void delete(Long id) {
        siteRepository.delete(id);
    }
}
