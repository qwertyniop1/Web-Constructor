package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.service.PageService;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/site")
public class ViewController {

    @Autowired
    UserService userService;

    @Autowired
    SiteService siteService;

    @Autowired
    PageService pageService;

    @GetMapping("/{user}/{site}")
    public String view(@PathVariable String user, @PathVariable String site,
                       Model model) {
        model.addAttribute("page", fixPages(siteService
                .getSite(userService.getUser(user), site)
                .getPages().iterator().next()));
        return "sites/view";
    }

    @GetMapping("/{user}/{site}/{page}")
    public String viewPage(@PathVariable String user, @PathVariable String site,
                       @PathVariable Long page, Model model) {
        model.addAttribute("page", fixPages(pageService.getPage(page)));
        return "sites/view";
    }

    private Page fixPages(Page page) {
        Set<Element> elements = page.getElements(); // TODO COSTYLI
        for (Element element : elements) {
            element.setPage(null);
        }
        page.setElements(elements);
        return page;
    }
}
