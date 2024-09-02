package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    void createUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByResetToken(String resetToken);
    void updateUser(User user, MultipartFile file) throws IOException;
    void updatePassword(String newPassword, Long userId);
    void updatePasswordByToken(String token, String newPassword);
    void deleteUserById(Long id);
    void deleteProfilePhoto(Long id);
    void processForgotPassword(String email);
    Boolean isExistUserByEmail(String email);
}
