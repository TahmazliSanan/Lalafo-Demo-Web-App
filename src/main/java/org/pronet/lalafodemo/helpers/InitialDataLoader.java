package org.pronet.lalafodemo.helpers;

import org.pronet.lalafodemo.entities.Role;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.enums.AuthenticationType;
import org.pronet.lalafodemo.repositories.RoleRepository;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Boolean isExistAdminRole = roleRepository.existsByName(AuthenticationType.Admin.name());
        Boolean isExistCustomerRole = roleRepository.existsByName(AuthenticationType.Customer.name());
        Role adminRole = roleRepository.findByName(AuthenticationType.Admin.name());
        Boolean isExistAdmin = userRepository.existsByRole(adminRole);
        if (!isExistAdminRole) {
            Role newAdminRole = new Role(1, AuthenticationType.Admin.name());
            roleRepository.save(newAdminRole);
        }
        if (!isExistCustomerRole) {
            Role newCustomerRole = new Role(2, AuthenticationType.Customer.name());
            roleRepository.save(newCustomerRole);
        }
        if (!isExistAdmin) {
            User admin = new User(
                    1L,
                    "Admin",
                    "of Lalafo",
                    "AdminOfLalafo",
                    "tehmezlisenan11@gmail.com",
                    passwordEncoder.encode("AdminOfLalafo2024"),
                    null,
                    null,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new Role(1, AuthenticationType.Admin.name()),
                    null,
                    null);
            userRepository.save(admin);
        }
    }
}
