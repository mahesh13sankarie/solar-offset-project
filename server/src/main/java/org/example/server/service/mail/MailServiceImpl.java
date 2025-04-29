package org.example.server.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.server.entity.MailAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author: astidhiyaa
 * @date: 28/03/25
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendEmail(MailAttributes mailAttributes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(message, true, "UTF-8");
        mailMessage.setTo(mailAttributes.recipient());
        mailMessage.setSubject(mailAttributes.subject());
        mailMessage.setText(mailAttributes.body(), mailAttributes.hyperlink());
        mailSender.send(message);
    }
}
