package org.pronet.lalafodemo.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.pronet.lalafodemo.services.SessionMessageManagementService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service
public class SessionMessageManagementServiceImplementation
        implements SessionMessageManagementService {
    @Override
    public void removeSessionMessage() {
        HttpServletRequest request = ((ServletRequestAttributes) (Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())))
                .getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");
    }
}
