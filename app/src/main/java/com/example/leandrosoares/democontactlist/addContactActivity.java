package com.example.leandrosoares.democontactlist;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class addContactActivity extends AppCompatActivity {


    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNumber;
    private EditText etZipCode;
    private EditText etDateOfBirth;
    private TextView titleProfile;

    private ImageView addContactButton;
    private ImageView returnButton;
    private ImageView profilePhoto;

    private ImageButton photoButton;

    private boolean isNewContact=true;

    private ImageView photoImageView;

    private int id;

    private int PICK_IMAGE=3;
    final int REQUEST_IMAGE_CAPTURE = 4;

    private TextWatcher textWatcher;

    byte[] photoByteArray=null;

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
        photoImageView=(ImageView) findViewById(R.id.photoImageView);


        //Get data from intent if it was called by edit Contact button

        Intent intent = getIntent();

        //Has data on intent
        if(intent.hasExtra("firstName")) {

            isNewContact=false;
            titleProfile.setText("Edit a Contact");


            //Get data from intent
            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String birthday = intent.getStringExtra("birthday");
            String phone = intent.getStringExtra("phone");
            String zipcode = intent.getStringExtra("zipcode");
            id= intent.getIntExtra("id",-1);
            byte[] photoByteArray= intent.getByteArrayExtra("photo");


            if(photoByteArray!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.length);
                photoImageView.setImageBitmap(bitmap);
            }

            etFirstName.setText(firstName);
            etLastName.setText(lastName);
            etDateOfBirth.setText(birthday);
            etPhoneNumber.setText(phone);
            etZipCode.setText(zipcode);
        }


        addContactButton= (ImageView) findViewById(R.id.addContactButton);
        returnButton= (ImageView) findViewById(R.id.returnButton);
        photoButton=(ImageButton)findViewById(R.id.photoButton);
        profilePhoto=(ImageView)findViewById(R.id.photoImageView);


        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(addContactActivity.this);
                CharSequence[] options= {"Take Photo", "Choose from gallery"};

                builder.setTitle("Change Photo")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                    }

                                }

                                else if(which==1){
                                    //Opens File Chooser
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


                                }

                            }
                        });
                builder.show();








            }
        });


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

                //Fields not empty and called to create a new contact
                if(!firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty() &&
                        !zipCode.isEmpty() && !dateOfBirth.isEmpty() && isNewContact)
                {
                    //Creates database
                    ContactsDbHelper mDbContactsHelper = new ContactsDbHelper(getApplicationContext());


                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbContactsHelper.getWritableDatabase();

                    //Put the values
                    ContentValues values = new ContentValues();
                    values.put(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,firstName );
                    values.put(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME, lastName);
                    values.put(ContactsDBContraints.contactEntry.COLUMN_BIRTH, dateOfBirth);
                    values.put(ContactsDBContraints.contactEntry.COLUMN_PHONE,phoneNumber );
                    values.put(ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE, zipCode );


                    if(photoByteArray!=null)
                        values.put(ContactsDBContraints.contactEntry.COLUMN_PHOTO, photoByteArray );

                    db.insert(ContactsDBContraints.contactEntry.TABLE_NAME, null, values);
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
                    values.put(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,firstName );
                    values.put(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME, lastName);
                    values.put(ContactsDBContraints.contactEntry.COLUMN_BIRTH, dateOfBirth);
                    values.put(ContactsDBContraints.contactEntry.COLUMN_PHONE,phoneNumber );
                    values.put(ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE, zipCode );

                    if(photoByteArray!=null){
                        values.put(ContactsDBContraints.contactEntry.COLUMN_PHOTO, photoByteArray );

                    }


                    db.update(ContactsDBContraints.contactEntry.TABLE_NAME,values, "_id="+id,null);


                    setResult(2);

                    finish();
                }

                //Fields empty
                else{

                    CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityAddContact);
                    Snackbar snackbar = Snackbar
                            .make( layout , "Please fill all the fields", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }


            }
        });


        this.etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

//        textWatcher = new TextWatcher(){
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//
//
//                etPhoneNumber.removeTextChangedListener(textWatcher);//after this line you do the editing code
//                String phoneNumberRaw=etPhoneNumber.getText().toString();
//                String locale= Locale.getDefault().getCountry();
//                Phonenumber.PhoneNumber numberProto = null;
//
//                AsYouTypeFormatter asYouTypeFormatter=phoneUtil.getAsYouTypeFormatter(locale);
//                asYouTypeFormatter.inputDigit()
//
//                try {
//                    numberProto = phoneUtil.parse(phoneNumberRaw, locale);
//                } catch (NumberParseException e) {
//                    e.printStackTrace();
//                }
//                //Since you know the country you can format it as follows:
//                etPhoneNumber.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
//
//                etPhoneNumber.addTextChangedListener(textWatcher); // you register again for listener callbacks
//
//            }};
//
//        etPhoneNumber.addTextChangedListener(textWatcher);







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3 && data != null){
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(bitmap!=null){
                profilePhoto.setImageBitmap(bitmap);
                }

            photoByteArray = getBitmapAsByteArray(bitmap);



        }

        if(requestCode==4&& data != null) {


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePhoto.setImageBitmap(imageBitmap);

            photoByteArray = getBitmapAsByteArray(imageBitmap);
        }



    }

    //Compress the bitmap and returns it as a byte array
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }
}
