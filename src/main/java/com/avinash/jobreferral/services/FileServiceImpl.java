package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;
import com.avinash.jobreferral.configuration.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private static final String CONTACT_NAME = "[Contact Name]";
    private static final String COMPANY_NAME = "[Company Name]";
    private static final String JOB_ROLE = "[Job Role]";
    @Autowired
    ConfigProperties myConfig;

    @Override
    public String readEmailTemplate() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(myConfig.getTemplateLocation()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("<br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String customizeMailContent(Contact contact) {
        String content = readEmailTemplate();

        return content.replace(CONTACT_NAME, contact.getFirstName())
                .replace(COMPANY_NAME, contact.getCompany())
                .replace(JOB_ROLE, contact.getJobRole() + " " + contact.getJobUrl());
    }

    @Override
    public void writeToCSV(List<Contact> contacts) throws IOException {

        FileWriter csvWriter = new FileWriter(myConfig.getContactLocation());
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
        List<Contact> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(myConfig.getContactLocation()))) {
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
