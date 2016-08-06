package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Element;
import by.itransition.webconstructor.domain.Page;
import by.itransition.webconstructor.dto.PageDto;
import by.itransition.webconstructor.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
        Page page = pageService.getPage(id);
        Set<Element> elements = page.getElements(); // TODO COSTYLI
        for (Element element : elements) {
            element.setPage(null);
        }
        page.setElements(elements);
        model.addAttribute("page", page);
//        model.addAttribute("elementList", pageService.getElements(id));
        return "constructor/index";
    }

    @PostMapping("/{page}")
    public @ResponseBody
    String update(@PathVariable("page") Long id, /*@RequestParam("layout") int layoutId,*/ @RequestBody PageDto response, Model model) {
        pageService.update(id, response);
        return "";
    }

    @DeleteMapping("/{page}")
    public @ResponseBody
    String remove(@PathVariable("page") Long id, Model model) {
        pageService.delete(id);
        return null;
    }

}
