package org.pronet.lalafodemo.security;

import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User foundedUser = userRepository.findByEmailContainingIgnoreCase(username.trim());
        if (foundedUser == null) {
            throw new UsernameNotFoundException("User is not found!");
        }
        return new CustomUserDetails(foundedUser);
    }
}
