package org.pronet.lalafodemo.services;

import org.pronet.lalafodemo.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void createUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    void updateUser(User user, MultipartFile file) throws IOException;
    void deleteUserById(Long id);
    Boolean isExistUserByEmail(String email);
}
