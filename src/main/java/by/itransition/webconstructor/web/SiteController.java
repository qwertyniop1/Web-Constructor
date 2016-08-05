package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sites")
public class SiteController {

    @Autowired
    SiteService siteService;

    @GetMapping
    public String view(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("sites", siteService.getSites(user));
        return "sites/list";
    }

    @GetMapping("/{site}")
    public String edit(@PathVariable("site") Long id, Model model) {
        model.addAttribute("pages", siteService.getPages(id));
        return "sites/site";
    }

    @GetMapping("/edit/{page}")
    public String constructor(@PathVariable("page") Long id, Model model) {
        return "constructor/index";
    }
}
