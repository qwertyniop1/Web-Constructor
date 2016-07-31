package by.itransition.webconstructor.web;

import by.itransition.webconstructor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private SiteService siteService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}
