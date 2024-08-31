package org.pronet.lalafodemo.controllers;

import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

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

    @GetMapping(value = "/view")
    public String myProfileView(
            Principal principal,
            Model model) {
        User loggedInUser = getLoggedInUserDetails(principal, model);
        model.addAttribute("loggedInUser", loggedInUser);
        return "profile/my-profile";
    }

    @PostMapping(value = "/update")
    public String updateMyProfile(
            @ModelAttribute(value = "user") User user,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session) throws IOException {
        userService.updateUser(user, file);
        session.setAttribute("successMessage", "Profiliniz uğurla yeniləndi!");
        return "redirect:/profile/view";
    }

    @GetMapping(value = "/delete-profile-image")
    public String deleteProfileImage(
            Principal principal,
            HttpSession session) {
        User user = getLoggedInUserDetails(principal);
        userService.deleteProfilePhoto(user.getId());
        session.setAttribute("successMessage", "Profil şəkliniz uğurla yeniləndi");
        return "redirect:/profile/view";
    }
}
