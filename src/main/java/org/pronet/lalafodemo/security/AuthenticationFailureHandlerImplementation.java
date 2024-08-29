package org.pronet.lalafodemo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pronet.lalafodemo.entities.User;
import org.pronet.lalafodemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureHandlerImplementation extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        User foundedUser = userRepository.findByEmailContainingIgnoreCase(email.trim());
        if (foundedUser != null) {
            if (foundedUser.getPassword().equals(password)) {
                response.sendRedirect("/");
            } else {
                exception = new BadCredentialsException("Şifrə yalnışdır!");
            }
        } else {
            exception = new UsernameNotFoundException("Müştəri mövcud deyil. Zəhmət olmasa yenidən cəhd edin!");
        }
        super.setDefaultFailureUrl("/auth/login-view?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
