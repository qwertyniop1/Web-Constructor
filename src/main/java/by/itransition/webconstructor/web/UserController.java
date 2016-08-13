package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.error.ResourceNotFoundException;
import by.itransition.webconstructor.service.SiteService;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SiteService siteService;

//TODO Security!!!
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping
    public String currentUser(Model model) {
        User user;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ex) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/user/" + user.getUsername();
    }

    @PostMapping
    public String update(@ModelAttribute  UserDto profile, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUser(user, profile);
        return "redirect:/user/";
    }

    @GetMapping("/{user}")
    public String profile(@PathVariable("user") String username, Model model) {
        User owner = null;
        try {
            owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ignored) {

        }
        User user = userService.getUser(username);
        model.addAttribute("user", user);
        model.addAttribute("profile", new UserDto(user)); //TODO don't show if this is not owner's profile
        model.addAttribute("owner", user.equals(owner));
        model.addAttribute("rates", siteService.getSitesRates(siteService.getSites(user)));
        return "user/profile";
    }

}
