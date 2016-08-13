package by.itransition.webconstructor.web;

import by.itransition.webconstructor.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    ApiService apiService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tagList", apiService.getAllTags());
        return "index";
    }

}
