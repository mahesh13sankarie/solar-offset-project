package org.example.server.service.mail;

import org.example.server.entity.MailAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    public void sendEmail(MailAttributes mailAttributes) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailAttributes.recipient());
        mailMessage.setSubject(mailAttributes.subject());
        mailMessage.setText(mailAttributes.body());
        mailSender.send(mailMessage);
    }
}
