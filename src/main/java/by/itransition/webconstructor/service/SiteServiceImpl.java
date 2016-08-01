package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    @Autowired
    private SiteRepository siteRepository;

    @Override
    public Site getSite(String user) {
        return null;
    }
}
