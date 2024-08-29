package org.pronet.lalafodemo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pronet.lalafodemo.enums.AuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Service
public class AuthenticationSuccessHandlerImplementation implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
        if (roles.contains(AuthenticationType.Admin.name())) {
            response.sendRedirect("/admin/");
        } else {
            response.sendRedirect("/");
        }
    }
}
