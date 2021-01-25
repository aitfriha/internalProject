package com.wproducts.administration.service.utilities;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

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
