package org.pronet.lalafodemo.controllers;

import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getLoggedInUserDetails(
            Principal principal,
            Model model) {
        if (principal != null) {
            String email = principal.getName();
            User foundedUser = userService.getUserByEmail(email);
            model.addAttribute("foundedUser", foundedUser);
        } else {
            model.addAttribute("foundedUser", null);
        }
    }

    @GetMapping(value = "/")
    public String homeView() {
        return "home/home";
    }
}
