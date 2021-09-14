package com.avinash.jobreferral.bean;

public class Contact implements Comparable<Contact> {
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String jobRole;
    private String jobUrl;
    private String alreadySent;

    public Contact() {
        jobRole = "Software Engineer";
        jobUrl = "";
        alreadySent = "NO";
    }

    public String getAlreadySent() {
        return alreadySent;
    }

    public void setAlreadySent(String alreadySent) {
        this.alreadySent = alreadySent;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", jobRole='" + jobRole + '\'' +
                ", jobUrl='" + jobUrl + '\'' +
                ", alreadySent='" + alreadySent + '\'' +
                '}';
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public int compareTo(Contact o) {
        return 0;
    }
}
