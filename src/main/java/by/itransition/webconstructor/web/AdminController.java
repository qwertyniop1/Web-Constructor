package by.itransition.webconstructor.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String index(Model model) {
        return "admin/index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        return null;
    }

    @GetMapping("/sites")
    public String sites(Model model) {
        return null;
    }

}
