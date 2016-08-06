package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public @ResponseBody
    String create(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.valueOf(siteService.create(user));
    }

    @GetMapping("/{site}")
    public String edit(@PathVariable("site") Long id, Model model) {
        model.addAttribute("site", siteService.getSite(id));
        model.addAttribute("pages", siteService.getPages(id));
        return "sites/site";
    }

    @PostMapping("/{site}")
    public String update(@PathVariable("site") Long id, @RequestParam String name, Model model) {
        siteService.update(id, name);
        return "redirect:/sites";
    }

    @DeleteMapping("/{site}")
    public @ResponseBody
    String remove(@PathVariable("site") Long id, Model model) {
        siteService.delete(id);
        return "";
    }

//    @GetMapping("/edit/{page}")
//    public String constructor(@PathVariable("page") Long id, Model model) {
//        return "constructor/index";
//    }
}
