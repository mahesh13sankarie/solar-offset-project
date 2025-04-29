package org.example.server.service.mail;

import jakarta.mail.MessagingException;
import org.example.server.entity.MailAttributes;

/**
 * @author: astidhiyaa
 * @date: 28/03/25
 */
public interface MailService {
    void sendEmail(MailAttributes mailAttributes) throws MessagingException;
}
