package org.techniu.isbackend.service;


public interface EmailSenderService {

    boolean sendSimpleMessage(String[] to, String subject, String message);
}
