package com.wproducts.administration.service.utilities;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailService {

    private void sendmail(String fromEmail, String fromName, String[] toEmails, String subject, String message) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("internal.system.project@gmail.com", "isystem2020");
            }
        });
        MimeMessage msg1 = new MimeMessage(session);
        MimeMessageHelper msg = new MimeMessageHelper(msg1, "UTF-8");
        msg.setFrom(fromEmail, fromName);
        for (String addr : toEmails) {
            msg.addTo(addr);
        }
        msg.setSubject("Tutorials point email");
        msg.setText("Tutorials point email", "text/html");
        msg.setSentDate(new Date());
    }
}
