package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;
import com.avinash.jobreferral.bean.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Service("mailService")
public class MailServiceImpl implements MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    FileService fileService;

    Queue<Contact> emailQueue = new PriorityQueue<>();

    public void prepareAndSendMail() throws IOException {
        List<Contact> records = fileService.readContactsFromFile();
        List<Contact> finalRecords = new ArrayList<>();

        for (Contact contact : records) {

            if(!contact.getAlreadySent().equalsIgnoreCase("Yes")){
                emailQueue.add(contact);
            }
            else{
                finalRecords.add(contact);
            }
        }
        if(emailQueue.isEmpty()){
            LOG.info("No new contacts present in file.");
        }

        while (!emailQueue.isEmpty()) {
            Contact contact = emailQueue.poll();
            Mail mail = new Mail();
            mail.setMailFrom("tingre.avinash@gmail.com");
            mail.setMailSubject("[REFERRAL REQUEST] Software Engineer | Java");
            mail.setMailTo(contact.getEmail());

            String content = fileService.formatEmailTemplate(contact);
            mail.setMailContent(content);

            try {
                sendMail(mail);
                contact.setAlreadySent("Yes");
                LOG.info("Mail successfully sent to: " + contact.getEmail());
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                LOG.error("Mail sending failed to: "+contact.getEmail());
                contact.setAlreadySent("No");
            }finally {
                finalRecords.add(contact);
            }

        }

        fileService.writeToCSV(finalRecords);
    }


    public void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject(mail.getMailSubject());
        mimeMessageHelper.setTo(mail.getMailTo());
        mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "Avinash Tingre"));
        mimeMessageHelper.setText(mail.getMailContent());

        mailSender.send(mimeMessageHelper.getMimeMessage());

    }
}
