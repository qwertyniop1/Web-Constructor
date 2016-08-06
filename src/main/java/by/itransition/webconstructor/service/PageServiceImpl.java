package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.repository.PageRepository;
import by.itransition.webconstructor.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PageServiceImpl implements PageService{

    @Autowired
    PageRepository pageRepository;

    @Autowired
    SiteRepository siteRepository;

    @Override
    public Page getPage(Long id) {
        Page page = pageRepository.findOne(id);
        if (page == null) {
            throw new ResourceNotFoundException();
        }
        return page;
    }

    @Override
    public long create(Long siteId) {
        Page page = new Page();
        page.setSite(siteRepository.findOne(siteId));
        return pageRepository.save(page).getId();
    }

    @Override
    public void delete(Long id) {
        pageRepository.delete(id);
    }
}
