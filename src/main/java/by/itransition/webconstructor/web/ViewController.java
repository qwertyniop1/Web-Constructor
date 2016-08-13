package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.service.PageService;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return String.format("redirect:/site/%s/%s/%d", user, site,
                siteService.getSite(userService.getUser(user), site)
                        .getPages().iterator().next().getId()); //FIXME 0 pages crash!!!
    }

    @GetMapping("/{owner}/{site}/{pageId}")
    public String viewPage(@PathVariable("owner") String username, @PathVariable String site,
                       @PathVariable("pageId") Long page, Model model) {
        User user = null;
        Page requestedPage = pageService.getPage(page);
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            model.addAttribute("rate", siteService.getRate(requestedPage.getSite().getId(),
                    user));
        } catch (ClassCastException ignored) {

        }
        model.addAttribute("page", fixPages(requestedPage));
        return "sites/view";
    }

    @PostMapping("/rate")
    public @ResponseBody
    String rate(@RequestParam Long site, @RequestParam double rate, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        siteService.setRate(site, rate, user);
        return "";
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
// TODO average rate count when delete mark