package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.repository.SiteRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private final SiteRepository siteRepository;

    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Site getSite(String user) {
        return null;
    }
}
