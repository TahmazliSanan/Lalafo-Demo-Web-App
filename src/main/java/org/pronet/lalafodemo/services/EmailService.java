package org.pronet.lalafodemo.services;

public interface EmailService {
    void sendResetPasswordLink(String receiverEmail, String resetLink);
}
