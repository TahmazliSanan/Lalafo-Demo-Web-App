package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    private User getLoggedInUserDetails(Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            return userService.getUserByEmail(email);
        } else {
            return null;
        }
    }

    @GetMapping(value = "/registration-view")
    public String registrationView() {
        return "auth/registration";
    }

    @PostMapping(value = "/register")
    public String register(
            @ModelAttribute(value = "user") User user,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            HttpSession session) {
        Boolean isExistUser = userService.isExistUserByEmail(user.getEmail());
        if (isExistUser) {
            session.setAttribute("errorMessage", "Müştəri artıq mövcuddur. " +
                    "Fərqli email ünvanı ilə qeydiyyatdan keçməyə cəhd edin!");
        } else {
            if (user.getPassword().equals(confirmPassword)) {
                userService.createUser(user);
                session.setAttribute("successMessage", "Qeydiyyat uğurla tamamlandı!");
            } else {
                session.setAttribute("errorMessage", "Şifrələr bir-birinə uyğun deyil!");
            }
        }
        return "redirect:/auth/registration-view";
    }

    @GetMapping(value = "/login-view")
    public String loadLoginPage() {
        return "auth/login";
    }

    @GetMapping(value = "/delete-view")
    public String deleteAccountView(
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal);
        model.addAttribute("loggedInUser", loggedInUser);
        return "auth/delete-account";
    }

    @PostMapping(value = "/delete")
    public String deleteAccount(
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        userService.deleteUserById(loggedInUser.getId());
        session.setAttribute("successMessage", "Hesabınız uğurla silindi!");
        return "redirect:/auth/login-view";
    }
}
