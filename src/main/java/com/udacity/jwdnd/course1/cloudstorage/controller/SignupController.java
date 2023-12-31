package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;
    public SignupController(UserService userService) {this.userService = userService; }

    @GetMapping()
    public String sigupView() {return "signup";}

    @PostMapping()
    public RedirectView signupResults(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;
        if (userService.isUsernameExists(user.getUsername())) {
            signupError = "The username already exists.";
        }
        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
                return new RedirectView("/signup", true);
            }
        }
        if (signupError == null) {
            //model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return new RedirectView("/login", true);
        }
        redirectAttributes.addAttribute("signupError", signupError);
        return new RedirectView("/signup", true);

    }

}
