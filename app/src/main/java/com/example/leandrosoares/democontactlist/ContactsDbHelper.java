package com.example.leandrosoares.democontactlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by leandrosoares on 24/05/17.
 */

import  com.example.leandrosoares.democontactlist.ContactsContract;

import java.util.ArrayList;

public class  ContactsDbHelper extends SQLiteOpenHelper {


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";



    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ContactsContract.contactEntry.TABLE_NAME + " (" +
                    ContactsContract.contactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactsContract.contactEntry.COLUMN_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactsContract.contactEntry.COLUMN_BIRTH + TEXT_TYPE + COMMA_SEP +
                    ContactsContract.contactEntry.COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
                    ContactsContract.contactEntry.COLUMN_ZIP_CODE + TEXT_TYPE +
                    ") ;";

    private static final String SQL_SELECT_ALL_NAMES=
            "SELECT  + " + ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME + COMMA_SEP +
                    ContactsContract.contactEntry.COLUMN_LAST_NAME +
                    " FROM " + ContactsContract.contactEntry.TABLE_NAME + ";";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactsContract.contactEntry.TABLE_NAME;




    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DemoContacts.db";

    public ContactsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<RowItem> selectAllContacts(SQLiteDatabase db){
        // Define a projection that specifies which columns from the database
// you will actually use after this query.

        ArrayList<RowItem> items = new ArrayList<RowItem>();

        String[] projection = {
                ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,
                ContactsContract.contactEntry.COLUMN_LAST_NAME,
                ContactsContract.contactEntry._ID

        };



        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME+ " ASC";

        Cursor c = db.query(
                ContactsContract.contactEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while (c.moveToNext()) {

            String first_name=c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME));
            String last_name=c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_LAST_NAME));
            int  id=c.getInt(c.getColumnIndexOrThrow(ContactsContract.contactEntry._ID));
            Log.d("Teste", first_name);
            String fullName=first_name+ " " + last_name;

            RowItem rowItem = new RowItem(fullName,id);
            items.add(rowItem);
        }






        return items;
    }

    public Contact selectContact(SQLiteDatabase db, int id){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

       Contact contact = null;

        String[] projection = {
                ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,
                ContactsContract.contactEntry.COLUMN_LAST_NAME,
                ContactsContract.contactEntry.COLUMN_PHONE,
                ContactsContract.contactEntry.COLUMN_BIRTH,
                ContactsContract.contactEntry.COLUMN_ZIP_CODE

        };


        String whereClause =
                ContactsContract.contactEntry._ID + " = " + " ? " ;



        String[] whereArgs = new String[] {
                String.valueOf(id)
        };





        // How you want the results sorted in the resulting Cursor


        Cursor c = db.query(
                ContactsContract.contactEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while (c.moveToNext()) {

            String first_name=c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME));
            String last_name=c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_LAST_NAME));
            String phoneNumber= c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_PHONE));
            String dateBirth= c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_BIRTH));
            String zipCode= c.getString(c.getColumnIndexOrThrow(ContactsContract.contactEntry.COLUMN_ZIP_CODE));

            Log.d("Main" , first_name+ " " + last_name);


            contact = new Contact(first_name,last_name,dateBirth,phoneNumber,zipCode);

        }






        return contact;
    }

    public int deleteContact(int id, SQLiteDatabase db){

        String table = ContactsContract.contactEntry.TABLE_NAME;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        return db.delete(table, whereClause, whereArgs);


    }




}