package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Value("${app.contactspath}")
    String contactsPath;

    @Value("${app.emailtemplate}")
    String emailTemplate;

    List<Contact> records = new ArrayList<>();

    @Override
    public String readEmailTemplate() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(emailTemplate))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String formatEmailTemplate(Contact contact) {
        String content = readEmailTemplate();

        String body = content.replace("[Contact Name]", contact.getFirstName())
                .replace("[Company Name]", contact.getCompany())
                .replace("[Job Role]", contact.getJobRole() + " " + contact.getJobUrl());
        return body;
    }

    @Override
    public void writeToCSV(List<Contact> contacts) throws IOException {

        FileWriter csvWriter = new FileWriter(contactsPath);
        try {
            csvWriter.append("First Name").append(",")
            .append("Last Name").append(",")
            .append("Email").append(",")
            .append("Company").append(",")
            .append("Job Role").append(",")
            .append("Job URL").append(",")
            .append("Mail Sent?").append("\n");

            for (Contact contact : contacts) {
                csvWriter.append(contact.getFirstName()).append(",")
                        .append(contact.getLastName()).append(",")
                        .append(contact.getEmail()).append(",")
                        .append(contact.getCompany()).append(",")
                        .append(contact.getJobRole()).append(",")
                        .append(contact.getJobUrl()).append(",")
                        .append(contact.getAlreadySent()).append("\n");

            }
        } finally {
            csvWriter.flush();
            csvWriter.close();
        }
    }

    @Override
    public List<Contact> readContactsFromFile() {

        try (BufferedReader br = new BufferedReader(new FileReader(contactsPath))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;

                if (count == 1) {
                    continue;
                }
                Contact contact = new Contact();
                String[] values = line.split(",");
                contact.setFirstName(values[0]);
                contact.setLastName(values[1]);
                contact.setEmail(values[2]);
                contact.setCompany(values[3]);

                if (values.length > 4 && !values[4].equals("")) contact.setJobRole(values[4]);
                if (values.length > 5 && !values[5].equals("")) contact.setJobUrl(values[5]);
                if (values.length > 6 && !values[6].equals("")) contact.setAlreadySent(values[6]);

                records.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
