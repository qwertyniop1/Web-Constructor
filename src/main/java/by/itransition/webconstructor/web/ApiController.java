package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.service.ApiService;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserService userService;

    @GetMapping("/tags.json")
    public List<Tag> getAllTags() {
        return apiService.getAllTags();
    }

    @GetMapping("/sites.json")
    public List<Site> sites(@RequestParam String user) {
        return siteService.getSites(userService.getUser(user));
    }

    @GetMapping("/rates.json")
    public Map<Long, Double> rates(@RequestParam String user) {
        return siteService.getSitesRates(siteService.getSites(userService.getUser(user)));
    }

//    public List<Tag> getSiteTags() {
//        return null;
//    }

}
