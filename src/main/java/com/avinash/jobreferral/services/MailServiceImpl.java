package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;
import com.avinash.jobreferral.bean.Mail;
import com.avinash.jobreferral.configuration.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("mailService")
public class MailServiceImpl implements MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);
    private static final String MAIL_SENT = "YES";
    private static final String MAIL_NOT_SENT = "NO";
    private static final List<Contact> successfulDeliveries = new ArrayList<>();
    private static final List<Contact> failedDeliveries = new ArrayList<>();
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    FileService fileService;
    @Autowired
    ConfigProperties myConfig;

    public List<Contact> getSuccessfulDeliveries() {
        return successfulDeliveries;
    }

    public List<Contact> getFailedDeliveries() {
        return failedDeliveries;
    }

    public void prepareAndSendMail() throws IOException {
        Queue<Contact> emailQueue = new PriorityQueue<>();

        List<Contact> records = fileService.readContactsFromFile();
        List<Contact> finalRecords = new ArrayList<>();

        for (Contact contact : records) {

            if (!contact.getAlreadySent().equalsIgnoreCase(MAIL_SENT)) {
                emailQueue.add(contact);
            } else {
                finalRecords.add(contact);
            }
        }
        if (emailQueue.isEmpty()) {
            LOG.info("No new contacts found.");
        }

        while (!emailQueue.isEmpty()) {
            Contact contact = emailQueue.poll();
            Mail mail = new Mail();

            mail.setMailTo(contact.getEmail());
            mail.setMailContent(fileService.customizeMailContent(contact));

            try {
                sendMail(mail);
                contact.setAlreadySent(MAIL_SENT);
                successfulDeliveries.add(contact);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                contact.setAlreadySent(MAIL_NOT_SENT);
                failedDeliveries.add(contact);
            } finally {
                finalRecords.add(contact);
            }

        }

        fileService.writeToCSV(finalRecords);
    }


    public void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject(myConfig.getSubject());
        mimeMessageHelper.setFrom(new InternetAddress(myConfig.getFromAddress(), myConfig.getFromName()));

        mimeMessageHelper.setTo(mail.getMailTo());
        mimeMessageHelper.setText(mail.getMailContent(), true);

        FileSystemResource resource = new FileSystemResource(new File(myConfig.getAttachment()));
        mimeMessageHelper.addAttachment(Objects.requireNonNull(resource.getFilename()), resource);

        mailSender.send(mimeMessageHelper.getMimeMessage());
    }
}