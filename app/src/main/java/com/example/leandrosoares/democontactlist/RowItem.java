package com.example.leandrosoares.democontactlist;

import android.graphics.Bitmap;

/**
 * Created by leandrosoares on 23/05/17.
 */
public class RowItem {

    private String contactName;
    private Bitmap avatar;


    public  RowItem(String contactName, Bitmap avatar){

        this.contactName=contactName;
        this.avatar=avatar;

    }
    public String getContactName(){

        return contactName;
    }

    public Bitmap getAvatar(){

        return  avatar;

    }



}
