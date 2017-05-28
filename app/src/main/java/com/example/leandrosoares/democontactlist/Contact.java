package com.example.leandrosoares.democontactlist;

/**
 * Created by leandrosoares on 27/05/17.
 * This class represents the atributes of a Contact
 *
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String zipCode;
    private byte[] photoByteArray;


    public Contact(String firstName, String lastName, String dateOfBirth, String phoneNumber, String zipCode,
    byte[] photoByte
    ){
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
        this.phoneNumber=phoneNumber;
        this.zipCode=zipCode;
        this.photoByteArray=photoByte;

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

    public byte[] getPhotoByteArray(){

        return photoByteArray;
    }
}
