package by.itransition.webconstructor.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/constructor")
public class EditorController {

    @GetMapping
    public String index(Model model) {
        return "constructor/index";
    }

}
