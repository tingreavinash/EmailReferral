package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;
import com.avinash.jobreferral.bean.Mail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MailService {
    void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException;

    void prepareAndSendMail() throws IOException;

    List<Contact> getSuccessfulDeliveries();

    List<Contact> getFailedDeliveries();
}
