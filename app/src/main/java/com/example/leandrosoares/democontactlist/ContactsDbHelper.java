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

    public ArrayList<String> selectAllContacts(SQLiteDatabase db){
        // Define a projection that specifies which columns from the database
// you will actually use after this query.

        ArrayList<String> names = new ArrayList<String>();

        String[] projection = {
                ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,
                ContactsContract.contactEntry.COLUMN_LAST_NAME

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
            Log.d("Teste", first_name);
            names.add(first_name+ " " + last_name);
        }






        return names;
    }




}