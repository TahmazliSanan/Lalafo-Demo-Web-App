package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.pronet.lalafodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
        foundedUser.setCreatedDateTime(LocalDateTime.now());
        userRepository.save(foundedUser);
        if (!file.isEmpty()) {
            File savedFile = new ClassPathResource("static/").getFile();
            Path path = Paths.get(
                    savedFile.getAbsolutePath() +
                            File.separator +
                            "profile-images" +
                            File.separator +
                            file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public Boolean isExistUserByEmail(String email) {
        return userRepository.existsByEmailContainingIgnoreCase(email.trim());
    }
}
