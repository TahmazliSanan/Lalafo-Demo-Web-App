package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserService userService;

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
}
