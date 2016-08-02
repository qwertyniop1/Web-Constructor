package by.itransition.webconstructor.web;

import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
        if (!userService.registerUser(user, request)) {
            result.rejectValue("email", "registration.userExist");
            return "auth/registration";
        }
        return "auth/complete";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("confirm_token") String token,
                          WebRequest request, Model model) {
        return userService.activateUser(token) ? "redirect:/login?activate=true" : "redirect:/auth-error";
    }

    @GetMapping("/auth-error")
    public String error(Model model) {
        model.addAttribute("authError", true);
        return "auth/complete";
    }

    @GetMapping("/resend-confirm")
    public String resend(Model model) {
        return null;
    }

}
