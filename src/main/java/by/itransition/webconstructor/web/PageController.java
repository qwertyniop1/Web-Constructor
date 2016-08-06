package by.itransition.webconstructor.web;

import by.itransition.webconstructor.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pages")
public class PageController {

    @Autowired
    PageService pageService;

    @PostMapping("/create")
    public @ResponseBody
    String create(@RequestParam("site") Long siteId, Model model) {
        return String.valueOf(pageService.create(siteId));
    }

    @GetMapping("/{page}")
    public String edit(@PathVariable("page") Long id, Model model) {
        model.addAttribute("page", pageService.getPage(id));
        return "constructor/index";
    }

    @PostMapping("/{page}")
    public String update(@PathVariable("page") Long id, Model model) {
        return null;
    }

    @DeleteMapping("/{page}")
    public @ResponseBody
    String remove(@PathVariable("page") Long id, Model model) {
        pageService.delete(id);
        return null;
    }

}
