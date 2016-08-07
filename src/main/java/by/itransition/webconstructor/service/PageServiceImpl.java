package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.Type;
import by.itransition.webconstructor.dto.ElementDto;
import by.itransition.webconstructor.dto.PageDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.repository.ElementsRepository;
import by.itransition.webconstructor.repository.PageRepository;
import by.itransition.webconstructor.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PageServiceImpl implements PageService{

    @Autowired
    PageRepository pageRepository;

    @Autowired
    SiteRepository siteRepository;

//    @Autowired
//    ElementsRepository elementsRepository;

    @Override
    public Page getPage(Long id) {
        Page page = pageRepository.findOne(id);
        if (page == null) {
            throw new ResourceNotFoundException();
        }
        return page;
    }

//    @Override
//    public List<Element> getElements(Long id) {
//        return elementsRepository.findByPage(pageRepository.findOne(id));
//    }

    @Override
    public long create(Long siteId) {
        Page page = new Page();
        page.setSite(siteRepository.findOne(siteId));
        return pageRepository.save(page).getId();
    }

    @Override
    public void update(Long id, PageDto pageDto) {
        Page page = pageRepository.findOne(id);
        page.setLayoutId(pageDto.getLayout());
        ElementDto[] elementDtos = pageDto.getElements();
        page.clearElements();
        for (ElementDto dto : elementDtos) {
            Element element = new Element();
            element.setLocation(dto.getLocation());
            element.setType(Type.valueOf(dto.getType().toUpperCase()));
            element.setPage(page);
            element.setWidth(dto.getWidth());
            element.setHeight(dto.getHeight());
            element.setUrl(dto.getUrl());
            element.setText(dto.getText());
            page.addElement(element);
        }
        pageRepository.save(page);
    }

    @Override
    public void delete(Long id) {
        pageRepository.delete(id);
    }
}
