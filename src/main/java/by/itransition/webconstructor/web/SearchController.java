package by.itransition.webconstructor.web;

import by.itransition.webconstructor.search.SiteSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SiteSearch siteSearch;

    @GetMapping
    public String search(@RequestParam String request, Model model) {
        model.addAttribute("results", siteSearch.search(request));
        return "search/index";
    }

}
