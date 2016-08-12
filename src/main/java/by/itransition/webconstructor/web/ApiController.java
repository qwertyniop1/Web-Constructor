package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.Tag;
import by.itransition.webconstructor.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/tags.json")
    public List<Tag> getAllTags() {
        return apiService.getAllTags();
    }

//    public List<Tag> getSiteTags() {
//        return null;
//    }

}
