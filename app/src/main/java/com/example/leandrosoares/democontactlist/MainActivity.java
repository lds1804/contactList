package com.example.leandrosoares.democontactlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int MAIN_ACTIVITY = 1;

    private ListView listViewContacts;

    private ContactListAdapter mContactListAdapter;

    private ArrayList<String> namesArray;


    private SQLiteDatabase db;


    private ContactsDbHelper mDbContactsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Creates database
        mDbContactsHelper = new ContactsDbHelper(this);


        // Gets the data repository in write mode
        db = mDbContactsHelper.getWritableDatabase();

        addDummyContactsDB(db);

        namesArray=mDbContactsHelper.selectAllContacts(db);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                


                Intent intent = new Intent( getApplicationContext(), addContactActivity.class);



                startActivityForResult(intent,MAIN_ACTIVITY);


            }
        });


        listViewContacts = (ListView) findViewById(R.id.contactsListView);










        mContactListAdapter= new ContactListAdapter(this,R.layout.contact,namesArray);

        listViewContacts.setAdapter(mContactListAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1){

            CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityMain);

            Snackbar snackbar = Snackbar
                    .make( layout , "Contact Added", Snackbar.LENGTH_LONG);

            snackbar.show();


            namesArray=mDbContactsHelper.selectAllContacts(db);

            mContactListAdapter= new ContactListAdapter(this,R.layout.contact,namesArray);

            listViewContacts.setAdapter(mContactListAdapter);





        }


    }

    private void addDummyContactsDB(SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Maria" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Silva");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1996-04-22");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Beyonce" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Joaquina");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);

        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Chris" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Rock");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Dominguinhos" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "do Rojao");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);

        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Jack Bauer" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "de Jesus");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Jose" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Dirceu");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Leandro" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Soares");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Lula" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "da Silva");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);



        values.clear();
        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Rihana" );
        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Umbrella");
        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );


        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
