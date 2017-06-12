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

import  com.example.leandrosoares.democontactlist.ContactsDBContraints;

import java.util.ArrayList;

/**
 * Class responsible to create, update and delete items on the table Contacts
 */

public class  ContactsDbHelper extends SQLiteOpenHelper {


    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    //Query to create the table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ContactsDBContraints.contactEntry.TABLE_NAME + " (" +
                    ContactsDBContraints.contactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_BIRTH + TEXT_TYPE + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE + TEXT_TYPE + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_PHOTO + BLOB_TYPE +
                    ") ;";

    private static final String SQL_SELECT_ALL_NAMES=
            "SELECT  + " + ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME + COMMA_SEP +
                    ContactsDBContraints.contactEntry.COLUMN_LAST_NAME +
                    " FROM " + ContactsDBContraints.contactEntry.TABLE_NAME + ";";

    //Query to delete entries
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactsDBContraints.contactEntry.TABLE_NAME;


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

    /**
     * get all contacts from the table contacts
     * @param db database to select from
     * @return
     */

    public ArrayList<RowItem> selectAllContacts(SQLiteDatabase db){
        // Define a projection that specifies which columns from the database
// you will actually use after this query.

        ArrayList<RowItem> items = new ArrayList<RowItem>();


        //Set which columns will be selected
        String[] projection = {
                ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,
                ContactsDBContraints.contactEntry.COLUMN_LAST_NAME,
                ContactsDBContraints.contactEntry._ID

        };



        // Set how the results will be sorted
        String sortOrder =
                ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME+ " ASC";


        //Queries the database
        Cursor c = db.query(
                ContactsDBContraints.contactEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        //iterates through the results using cursor and adds to the list items
        while (c.moveToNext()) {

            String first_name=c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME));
            String last_name=c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME));

            int  id=c.getInt(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry._ID));

            Log.d("Teste", first_name);

            String fullName=first_name+ " " + last_name;

            RowItem rowItem = new RowItem(fullName,id);
            items.add(rowItem);
        }


        return items;
    }


    /**
     * Returns the contact that corresponds to the given id
     * @param db database to query
     * @param id of the contact
     * @return a contact
     */
    public Contact selectContact(SQLiteDatabase db, int id){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

       Contact contact = null;


        //The fields to be returned
        String[] projection = {
                ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,
                ContactsDBContraints.contactEntry.COLUMN_LAST_NAME,
                ContactsDBContraints.contactEntry.COLUMN_PHONE,
                ContactsDBContraints.contactEntry.COLUMN_BIRTH,
                ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE,
                ContactsDBContraints.contactEntry.COLUMN_PHOTO


        };

        //Where clause to be used on the query
        String whereClause =
                ContactsDBContraints.contactEntry._ID + " = " + " ? " ;


        //Arguments used on the where clause
        String[] whereArgs = new String[] {
                String.valueOf(id)
        };





        Cursor c = db.query(
                ContactsDBContraints.contactEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        //Store the value of the query on contact
        while (c.moveToNext()) {

            String first_name=c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME));
            String last_name=c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME));
            String phoneNumber= c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_PHONE));
            String dateBirth= c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_BIRTH));
            String zipCode= c.getString(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE));
            byte[] photoByteArray=c.getBlob(c.getColumnIndexOrThrow(ContactsDBContraints.contactEntry.COLUMN_PHOTO));

            Log.d("Main" , first_name+ " " + last_name);


           // contact = new Contact(name,last_name,dateBirth,phoneNumber,zipCode,photoByteArray);

        }

        return contact;

    }

    /**
     * Delete a contact from the database
     * @param id of the contact
     * @param db database to delete the contact from
     * @return
     */
    public int deleteContact(int id, SQLiteDatabase db){

        String table = ContactsDBContraints.contactEntry.TABLE_NAME;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        return db.delete(table, whereClause, whereArgs);


    }




}