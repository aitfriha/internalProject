package org.techniu.isbackend.service.utilities;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static javax.xml.transform.OutputKeys.ENCODING;

public class MailMail{
    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String fromEmail, String fromName, String[] toEmails, String subject, String msg) {

        MimeMessagePreparator mailMessage = mimeMessage -> {

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                message.setFrom(fromEmail, fromName);
                for (String addr : toEmails) {
                    message.addTo(addr);
                }
                message.setReplyTo(fromEmail);
                message.setSubject(subject);
                message.setText(msg, true);
        };
        mailSender.send(mailMessage);
    }
}
