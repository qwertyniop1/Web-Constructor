package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.CommentDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.service.PageService;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Set<Page> pages = siteService.getSite(userService.getUser(user), site).getPages();
        if (pages.size() == 0) {
            throw new ResourceNotFoundException();
        }
        List<Page> pageList = new ArrayList<>(pages);
        pageList.sort(null);
        return String.format("redirect:/site/%s/%s/%d", user, site,pageList.get(0).getId());
    }

    @GetMapping("/{owner}/{site}/{pageId}")
    public String viewPage(@PathVariable("owner") String username, @PathVariable String site,
                       @PathVariable("pageId") Long page, Model model) {
        User user;
        Page requestedPage = pageService.getUserPage(page, username, site);
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

    @GetMapping("/elements")
    public @ResponseBody
    List<Element> getPageElements(@RequestParam Long page, Model model) {
        return pageService.getElements(page);
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