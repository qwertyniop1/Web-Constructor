package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Site;
import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.service.ApiService;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
    public List<Site> sites(@RequestParam(required = false) String user) {
        return user != null
                ? siteService.getSites(userService.getUser(user))
                : siteService.getAllSites();
    }

    @GetMapping("/topSites.json")
    public List<Site> topSites(@RequestParam(required = false) Integer quantity) {
        return siteService.getTopSites(quantity);
    }

    @GetMapping("/rates.json")
    public Map<Long, Double> rates(@RequestParam String user) {
        return siteService.getSitesRates(siteService.getSites(userService.getUser(user)));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/users.json")
    public @ResponseBody
    List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/topUsers.json")
    List<UserDto> getTopUsers(@RequestParam(required = false) Integer quantity) {
        return apiService.getTopUsers(quantity);
    }

//    public List<Tag> getSiteTags() {
//        return null;
//    }

}
