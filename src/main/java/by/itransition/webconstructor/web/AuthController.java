package by.itransition.webconstructor.web;

import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDto user,
                           BindingResult result, HttpServletRequest request,
                           Errors errors, Model model) {
        model.addAttribute(user);
        if (result.hasErrors()) {
            return "auth/registration";
        }
        if (!userService.registerUser(user)) {
            result.rejectValue("email", "registration.userExist");
            return "auth/registration";
        }
        return "auth/success";
    }

}
