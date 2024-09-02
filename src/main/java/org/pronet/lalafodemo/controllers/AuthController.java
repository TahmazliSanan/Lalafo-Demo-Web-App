package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public User getLoggedInUserDetails(
            Principal principal,
            Model model) {
        if (principal != null) {
            String email = principal.getName();
            User foundedUser = userService.getUserByEmail(email);
            model.addAttribute("foundedUser", foundedUser);
            return foundedUser;
        } else {
            return null;
        }
    }

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

    @GetMapping(value = "/change-account-password-view")
    public String changeAccountPasswordView() {
        return "auth/change-account-password";
    }

    @PostMapping(value = "/change-account-password")
    public String changeAccountPassword(
            @RequestParam(value = "currentPassword") String currentPassword,
            @RequestParam(value = "newPassword") String newPassword,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            Principal principal,
            HttpSession session) {
        User loggedInUser = getLoggedInUserDetails(principal);
        if (passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            if (newPassword.equals(confirmPassword)) {
                userService.updatePassword(newPassword, loggedInUser.getId());
                session.setAttribute("successMessage", "Şifrəniz uğurla dəyişdirildi!");
            } else {
                session.setAttribute("errorMessage", "Şifrələr bir-birinə uyğun deyil!");
            }
        } else {
            session.setAttribute("errorMessage", "Daxil etdiyiniz şifrə doğru deyil!");
        }
        return "redirect:/auth/change-account-password-view";
    }

    @GetMapping(value = "/forgot-password-view")
    public String forgotPasswordView() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam(value = "email") String email,
            HttpSession session) {
        Boolean isExistUser = userService.isExistUserByEmail(email);
        if (isExistUser) {
            userService.processForgotPassword(email);
            session.setAttribute("successMessage", "Link " + email + " email ünvanına göndərildi!");
        } else {
            session.setAttribute("errorMessage", "Email ünvanı mövcud deyil!");
        }
        return "redirect:/auth/forgot-password-view";
    }

    @GetMapping(value = "/reset-password-view")
    public String resetPasswordView(
            @RequestParam(value = "token") String token,
            Model model,
            HttpSession session) {
        User user = userService.getUserByResetToken(token);
        if (user == null || user.getTokenExpirationDate().before(new Date())) {
            session.setAttribute("errorMessage", "Link yanlış və ya vaxtı bitmişdir!");
            return "auth/reset-password";
        }
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping(value = "/reset-password")
    public String processResetPassword(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "newPassword") String newPassword,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            HttpSession session) {
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("errorMessage", "Şifrələr bir-birinə uyğun deyil!");
        } else {
            userService.updatePasswordByToken(token, newPassword);
            session.setAttribute("successMessage", "Şifrəniz uğurla yeniləndi!");
        }
        return "redirect:/auth/reset-password-view?token=" + token;
    }
}
