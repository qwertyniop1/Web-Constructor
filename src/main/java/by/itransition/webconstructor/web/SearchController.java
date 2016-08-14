package by.itransition.webconstructor.web;

import by.itransition.webconstructor.search.PageSearch;
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
    private PageSearch pageSearch;

    @GetMapping
    public String search(@RequestParam(required = false) String request, Model model) {
        if (request != null && request.trim().length() > 0) {
            model.addAttribute("results", pageSearch.search(request));
        }
        return "search/index";
    }

}
