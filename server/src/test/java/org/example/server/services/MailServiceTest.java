package org.example.server.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.server.entity.MailAttributes;
import org.example.server.service.mail.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: astidhiyaa
 * @date: 08/05/25
 */
@ExtendWith(MockitoExtension.class)
public class MailServiceTest {
    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    String content = "<html><body>" +
            "<p>Please click the following link: " +
            "<a href=\"" + "link" + "\">Reset password</a></p>" +
            "<p>Best regards,<br/>Syntax Squad</p>" +
            "</body></html>";


    @Test
    void send_email_success() throws MessagingException {
        // given
        MailAttributes mailAttributes = new MailAttributes(
                "recipient@example.com",
                "Test Subject",
                "Email Body",
                content
        );

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        mailService.sendEmail(mailAttributes);

        // then
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }
}
