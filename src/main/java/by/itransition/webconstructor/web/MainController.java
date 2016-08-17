package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.service.ApiService;
import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Integer USERS_QUANTITY = 5;
    private static final Integer SITES_QUANTITY = 10;

    @Autowired
    private ApiService apiService;

    @Autowired
    private SiteService siteService;

    @GetMapping("/")
    public String index(Model model) {
        List<Site> siteList = siteService.getTopSites(SITES_QUANTITY);
        model.addAttribute("tagList", apiService.getTopTags());
        model.addAttribute("topUsers", apiService.getTopUsers(USERS_QUANTITY));
        model.addAttribute("topSites", siteList);
        model.addAttribute("rates", siteService.getSitesRates(siteList));
        return "index";
    }

}
