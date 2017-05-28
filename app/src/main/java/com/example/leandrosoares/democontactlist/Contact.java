package com.example.leandrosoares.democontactlist;

/**
 * Created by leandrosoares on 27/05/17.
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String zipCode;


    public Contact(String firstName, String lastName, String dateOfBirth, String phoneNumber, String zipCode){
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
        this.phoneNumber=phoneNumber;
        this.zipCode=zipCode;



    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }
}
