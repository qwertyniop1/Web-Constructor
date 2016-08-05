package by.itransition.webconstructor.web;

import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sites")
public class SiteController {

    @Autowired
    SiteService siteService;

    @GetMapping
    public String view(Model model) {
        model.addAttribute("sites", siteService.getSites("pidr"));
        return "sites/list";
    }

}
