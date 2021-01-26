package org.techniu.isbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.techniu.isbackend.exception.EntityType.EmailAddress;


@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    private JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public boolean sendSimpleMessage(String[] to, String subject, String message) {
        boolean send = true;
        try {
            LOGGER.info("Sending emails...");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("internal.system.project@gmail.com");
            helper.setText(message, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            send = false;
            LOGGER.error("Error when sending emails", e);
        }
        LOGGER.info("Email send!!!");
        return send;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EmailAddress, exceptionType, args);
    }


}
