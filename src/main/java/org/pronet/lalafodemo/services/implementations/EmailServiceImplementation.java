package org.pronet.lalafodemo.services.implementations;

import org.pronet.lalafodemo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendResetPasswordLink(String receiverEmail, String resetLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("Şifrəni yeniləmə linki");
        mailMessage.setText("Şifrəni yeniləmək üçün aşağıdakı linkə daxil olun:\n" + resetLink);
        mailMessage.setFrom("admin.lalafo@gmail.com");
        mailSender.send(mailMessage);
    }
}
