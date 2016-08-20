package by.itransition.webconstructor.web;

import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.dto.UserDto;
import by.itransition.webconstructor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model) {
        return "admin/index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        return "admin/users";
    }

    @GetMapping("/user/{username}")
    public String user(@PathVariable String username, Model model) {
        model.addAttribute("user", new UserDto(userService.getUser(username)));
        return "admin/user";
    }

    @PostMapping("/user/{username}")
    public String edit(@PathVariable String username,
                       @ModelAttribute("user") @Valid UserDto user,
                       BindingResult result, Model model) {
        if (result.hasErrors()
                && !(user.getPassword().length() == 0
                && result.getErrorCount() == 2)) {
            model.addAttribute("user", user);
            return "admin/user";
        }
        if (!userService.editUser(username, user)) {
            model.addAttribute("user", user);
            result.rejectValue("email", "registration.userExist");
            return "admin/user";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/user")
    public String currentUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "redirect:/admin/user/" + user.getUsername();
    }

    @PostMapping("/ban")
    public @ResponseBody
    String banUser(@RequestParam String username) {
        userService.banUser(username);
        return "OK";
    }

    @PostMapping("/unban")
    public @ResponseBody
    String unbanUser(@RequestParam String username) {
        userService.unbanUser(username);
        return "OK";
    }

    @PostMapping("/delete")
    public @ResponseBody
    String deleteUser(@RequestParam String username) {
        userService.deleteUser(username);
        return "OK";
    }

    @PostMapping("/make-admin")
    public @ResponseBody
    String makeAdmin(@RequestParam String username) {
        userService.makeAdmin(username);
        return "OK";
    }

    @PostMapping("/make-user")
    public @ResponseBody
    String makeUser(@RequestParam String username) {
        userService.makeUser(username);
        return "OK";
    }

    @GetMapping("/sites")
    public String sites(Model model) {
        return "admin/sites";
    }

}
