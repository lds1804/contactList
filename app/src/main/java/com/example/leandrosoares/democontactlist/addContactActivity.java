package com.example.leandrosoares.democontactlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class addContactActivity extends AppCompatActivity {


    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNumber;
    private EditText etZipCode;
    private EditText etDateOfBirth;
    private TextView titleProfile;

    private ImageView addContactButton;
    private ImageView returnButton;

    private boolean isNewContact=true;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        etFirstName= (EditText) findViewById(R.id.first_name);
        etLastName= (EditText) findViewById(R.id.last_name);
        etPhoneNumber= (EditText) findViewById(R.id.phone_number);
        etDateOfBirth= (EditText) findViewById(R.id.date_birth);
        etZipCode= (EditText) findViewById(R.id.zipcode);
        titleProfile= (TextView) findViewById(R.id.titleProfile);




        //Get data

        Intent intent = getIntent();

        if(intent.hasExtra("firstName")) {

            isNewContact=false;

            titleProfile.setText("Edit a Contact");



            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String birthday = intent.getStringExtra("birthday");
            String phone = intent.getStringExtra("phone");
            String zipcode = intent.getStringExtra("zipcode");
            id= intent.getIntExtra("id",-1);

            etFirstName.setText(firstName);
            etLastName.setText(lastName);
            etDateOfBirth.setText(birthday);
            etPhoneNumber.setText(phone);
            etZipCode.setText(zipcode);
        }


        addContactButton= (ImageView) findViewById(R.id.addContactButton);
        returnButton= (ImageView) findViewById(R.id.returnButton);


        //Returns to the contacts activity
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName= String.valueOf(etFirstName.getText());
                String lastName= String.valueOf(etLastName.getText());
                String phoneNumber= String.valueOf(etPhoneNumber.getText());
                String zipCode= String.valueOf(etZipCode.getText());
                String dateOfBirth= String.valueOf(etDateOfBirth.getText());


                if(!firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty() &&
                        !zipCode.isEmpty() && !dateOfBirth.isEmpty() && isNewContact)
                {
                    //Creates database
                    ContactsDbHelper mDbContactsHelper = new ContactsDbHelper(getApplicationContext());


                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbContactsHelper.getWritableDatabase();


                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,firstName );
                    values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, lastName);
                    values.put(ContactsContract.contactEntry.COLUMN_BIRTH, dateOfBirth);
                    values.put(ContactsContract.contactEntry.COLUMN_PHONE,phoneNumber );
                    values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, zipCode );


                    db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);


                    setResult(1);

                    finish();






                }


                //Update Contact
                else if(!isNewContact){

                    //Creates database
                    ContactsDbHelper mDbContactsHelper = new ContactsDbHelper(getApplicationContext());


                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbContactsHelper.getWritableDatabase();


                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,firstName );
                    values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, lastName);
                    values.put(ContactsContract.contactEntry.COLUMN_BIRTH, dateOfBirth);
                    values.put(ContactsContract.contactEntry.COLUMN_PHONE,phoneNumber );
                    values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, zipCode );


                    db.update(ContactsContract.contactEntry.TABLE_NAME,values, "_id="+id,null);


                    setResult(2);

                    finish();




                }

                else{

                    CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityAddContact);

                    Snackbar snackbar = Snackbar
                            .make( layout , "Please fill all the fields", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }


            }
        });




    }

}
