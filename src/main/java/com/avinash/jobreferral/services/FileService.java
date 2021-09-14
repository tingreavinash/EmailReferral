package com.avinash.jobreferral.services;

import com.avinash.jobreferral.bean.Contact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    String readEmailTemplate();
    String formatEmailTemplate(Contact contact) throws FileNotFoundException;
    List<Contact> readContactsFromFile() throws FileNotFoundException;
    void writeToCSV(List<Contact> contacts) throws IOException;
}
