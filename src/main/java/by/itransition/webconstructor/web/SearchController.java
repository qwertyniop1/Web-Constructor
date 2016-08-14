package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.search.EntitySearch;
import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    private static final String[] PAGE_FIELDS = {"name", "comments.content", "elements.text"};
    private static final String[] SITE_FIELDS = {"name", "description"};
    private static final String[] USER_FIELDS = {"firstname", "lastname"};

    @Autowired
    private EntitySearch entitySearch;

    @Autowired
    private SiteService siteService;

    @GetMapping
    public String search(@RequestParam(required = false) String request,
                         @RequestParam(required = false) String tag, Model model) {
        if (request != null && request.trim().length() > 0) {
            model.addAttribute("results", entitySearch.search(request, Page.class, PAGE_FIELDS));
            model.addAttribute("sites", entitySearch.search(request, Site.class, SITE_FIELDS));
            model.addAttribute("users", entitySearch.search(request, User.class, USER_FIELDS));
        }
        if (tag != null) {
            model.addAttribute("sites", siteService.findByTag(tag));
        }
        return "search/index";
    }

}
