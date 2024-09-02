package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.Role;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.enums.AuthenticationType;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.pronet.lalafodemo.services.EmailService;
import org.pronet.lalafodemo.services.UserService;
import org.pronet.lalafodemo.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public void createUser(User user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(new Role(2, AuthenticationType.Customer.name()));
        newUser.setResetToken(null);
        newUser.setTokenExpirationDate(null);
        userRepository.save(newUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email.trim());
    }

    @Override
    public User getUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public void updateUser(User user, MultipartFile file)
            throws IOException {
        User foundedUser = getUserById(user.getId());
        String imageName = file.isEmpty() ? foundedUser.getImageName() : file.getOriginalFilename();
        foundedUser.setFirstName(user.getFirstName().trim());
        foundedUser.setLastName(user.getLastName().trim());
        foundedUser.setUsername(user.getUsername().trim());
        foundedUser.setBirthDate(user.getBirthDate());
        foundedUser.setImageName(imageName);
        foundedUser.setResetToken(null);
        foundedUser.setTokenExpirationDate(null);
        userRepository.save(foundedUser);
        if (!file.isEmpty()) {
            File savedFile = new ClassPathResource("static/").getFile();
            Path path = Paths.get(
                    savedFile.getAbsolutePath() +
                            File.separator +
                            "customer-images" +
                            File.separator +
                            file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void updatePassword(String newPassword, Long userId) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updatePasswordByToken(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setTokenExpirationDate(null);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public void deleteProfilePhoto(Long id) {
        User foundedUser = getUserById(id);
        foundedUser.setImageName(null);
        userRepository.save(foundedUser);
    }

    @Override
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmailContainingIgnoreCase(email.trim());
        String token = TokenUtil.generateResetToken();
        user.setResetToken(token);
        user.setTokenExpirationDate(TokenUtil.calculateTokenExpiryDate(60));
        userRepository.save(user);
        String resetLink = "http://localhost:8080/auth/reset-password-view?token=" + token;
        emailService.sendResetPasswordLink(email, resetLink);
    }

    @Override
    public Boolean isExistUserByEmail(String email) {
        return userRepository.existsByEmailContainingIgnoreCase(email.trim());
    }
}
