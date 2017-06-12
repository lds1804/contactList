package com.example.leandrosoares.democontactlist;

/**
 * Created by leandrosoares on 27/05/17.
 * This class represents the atributes of a Contact
 *
 */
public class Contact {

    private String name;
    private String phoneNumber;
    private String email;
    private byte[] photoByteArray;


    public Contact(String name, String phoneNumber, String email,
    byte[] photoByte
    ){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.photoByteArray=photoByte;

    }



    public String getName() {
        return name;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPhotoByteArray(){

        return photoByteArray;
    }
}
