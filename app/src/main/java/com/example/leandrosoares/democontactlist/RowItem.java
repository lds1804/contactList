package com.example.leandrosoares.democontactlist;

import android.graphics.Bitmap;

/**
 * Created by leandrosoares on 23/05/17.
 */
public class RowItem {

    private String contactName;
    private int id;


    public  RowItem(String contactName, int id){

        this.contactName=contactName;
        this.id=id;

    }
    public String getContactName(){

        return contactName;
    }

    public int getId(){

        return  id;

    }



}
