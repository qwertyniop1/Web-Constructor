package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.repository.PageRepository;
import by.itransition.webconstructor.repository.SiteRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private PageRepository pageRepository;


    @Override
    public List<Site> getSites(User user) {
        return siteRepository.findByUser(user);
    }

    @Override
    public List<Page> getPages(Long siteId) {
        return pageRepository.findBySite(siteRepository.findOne(siteId));
    }
}
