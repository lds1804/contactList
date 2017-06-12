package com.example.leandrosoares.democontactlist;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    static final int MAIN_ACTIVITY = 1;
    static final int CONTACT_EDIT = 2;

    private Context context;

    private ListView listViewContacts;

    private ContactListAdapter mContactListAdapter;

    private ArrayList<RowItem> itemsContact;

    private SQLiteDatabase db;

    private ContactsDbHelper mDbContactsHelper;

    private SlidingUpPanelLayout mSlidePanelLayout;


    private TextView tvFirstName;
    private TextView tvPhoneNumber;
    private TextView tvEmail;


    private ImageView backgroundProfile;
    private ImageView nameIcon;
    private ImageView phoneIcon;
    private ImageView emailIcon;
    private ImageView starIcon;


    private ImageButton removeContactMenuButton;
    private ImageButton editContact;

    private int rowID;

    private Contact contact;


    SharedPreferences prefs = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        context=this;




        //Get preferences to check if it is the first run
        prefs = getSharedPreferences("com.example.leandrosoares.democontactlist", MODE_PRIVATE);

        //Creates database
        mDbContactsHelper = new ContactsDbHelper(this);


        // Gets the data repository in write mode
        db = mDbContactsHelper.getWritableDatabase();

        //Get all the contacts on the database
        itemsContact=mDbContactsHelper.selectAllContacts(db);


        //Get Contacts from the phone
        itemsContact=getAllContactsNames();

        //Organize list alphabetically
        if (itemsContact.size() > 0) {
            Collections.sort(itemsContact, new Comparator<RowItem>() {
                @Override
                public int compare(RowItem rowItem, RowItem t1) {
                    return rowItem.getContactName().compareTo(t1.getContactName());
                }


            } );
        }

        //

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start activity to add a new contact
                Intent intent = new Intent( getApplicationContext(), addContactActivity.class);
                startActivityForResult(intent,MAIN_ACTIVITY);

            }
        });


        listViewContacts = (ListView) findViewById(R.id.contactsListView);
        mSlidePanelLayout= (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        tvFirstName= (TextView) findViewById(R.id.tvFirstName);
        tvPhoneNumber= (TextView) findViewById(R.id.tvPhoneNumber);
        tvEmail= (TextView) findViewById(R.id.tvEmail);



        backgroundProfile=(ImageView) findViewById(R.id.backgroundProfile);
        nameIcon=(ImageView) findViewById(R.id.name_icon);

        emailIcon=(ImageView) findViewById(R.id.iconEmail);
        phoneIcon=(ImageView) findViewById(R.id.iconPhone);

        removeContactMenuButton= (ImageButton) findViewById(R.id.menuRemoveContact);
        editContact= (ImageButton) findViewById(R.id.editContactButton);


        //Creates and sets the adapter to the listView
        mContactListAdapter= new ContactListAdapter(this,R.layout.contact,itemsContact);
        listViewContacts.setAdapter(mContactListAdapter);



        //Sets on click listener on each item of the list view
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                RowItem rowItem= (RowItem) parent.getItemAtPosition(position);
                rowID=rowItem.getId();

                //Expands the Slide Panel
                mSlidePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                fab.setVisibility(View.INVISIBLE);

                contact= getContact(rowID,rowItem.getContactName());

                //Fill the fields of the profile panel
                tvFirstName.setText(contact.getName());

                tvPhoneNumber.setText(contact.getPhoneNumber());

                tvEmail.setText(contact.getEmail());

                byte[] photoData=contact.getPhotoByteArray();


                if(photoData!=null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
                    backgroundProfile.setImageBitmap(bitmap);
                    backgroundProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);



                }

                else{

                    backgroundProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }


                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

                // generate color based on a key (same key returns the same color), useful for list/grid views
                int color = generator.getColor(contact.getName());

                //Sets the color of the icons to the color generated
                backgroundProfile.setBackgroundColor(color);
                nameIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                phoneIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                emailIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            }
        });


        mSlidePanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }


            //Makes the floating button visible when Slide panel Collapses
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {



                if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    fab.setVisibility(View.VISIBLE);
                    int id = getResources().getIdentifier("com.example.leandrosoares.democontactlist:drawable/" + "ic_person", null, null);
                    backgroundProfile.setImageResource(id);

                }


            }
        });


        //button remove contact clicked
        removeContactMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creates dialog to show options to the user
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you really to remove this contact?");
                builder.setNegativeButton("No", null);


                //Sets positive button
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        mDbContactsHelper.deleteContact(rowID,db);
                        CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityMain);
                        Snackbar snackbar = Snackbar
                                .make( layout , "Contact Deleted", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        refreshListView();
                        mSlidePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);



                    }});


                builder.show();
            }
        });


        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create activity to edit contact
                Intent intent =new Intent(getApplicationContext(),addContactActivity.class);

                intent.putExtra("name",contact.getName());
                intent.putExtra("phone",contact.getPhoneNumber());
                intent.putExtra("email",contact.getEmail());
                intent.putExtra("photo",contact.getPhotoByteArray());
                intent.putExtra("id", rowID);


                startActivityForResult(intent,CONTACT_EDIT);

            }
        });

        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone= (String) tvPhoneNumber.getText();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);

            }
        });

        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone= (String) tvPhoneNumber.getText();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });


    }

    private Contact getContact(int rowID, String name) {


        String phoneNumber="";
        String email="";
        byte[] photo=null;

        ContentResolver cr = getContentResolver();

        String[] PHONES_PROJECTION = new String[] { "_id","display_name","data1","data3"};//

        //  Get all phone numbers.
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + rowID, null, null);


        while (phones.moveToNext()) {

            //TODO organizar telefones por tipo
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            //TODO removed by now
//            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//            switch (type) {
//                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                    phoneNumber=number;
//                    break;
//                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                    phoneNumber=number;
//                    break;
//                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                    phoneNumber=number;
//                    break;
//            }
        }
        phones.close();




        //email
        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI ,
                new String[]{"_id","data1","data2","data3"}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + rowID , null, null);
        while(emailCur.moveToNext()) {
            int i = emailCur.getInt(0);
            email = emailCur.getString(1);
            email = emailCur.getString(2);
            email = emailCur.getString(3);
        }
        emailCur.close();


        //photo
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowID);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);

        AssetFileDescriptor fd = null;
        try {
            fd = getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
            InputStream inputStream=fd.createInputStream();
            photo = readBytes(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        contact = new Contact(name,phoneNumber,email,photo);



        return contact;



    }



    private void getAllContactsDetails() {


        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()){
            //get name
            int nameFiledColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFiledColumnIndex);

            String[] PHONES_PROJECTION = new String[] { "_id","display_name","data1","data3"};//
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            //name type ..
            while(phone.moveToNext()) {
                int i = phone.getInt(0);
                String str = phone.getString(1);
                String phoneNumberString = phone.getString(2);
                String email = phone.getString(3);
            }
            phone.close();
            //addr
            Cursor addrCur = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI ,
                    new String[]{"_id","data1","data2","data3"}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId , null, null);
            while(addrCur.moveToNext()) {
                int i = addrCur.getInt(0);
                String str = addrCur.getString(1);
                str = addrCur.getString(2);
                str = addrCur.getString(3);
            }
            addrCur.close();

            //email
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI ,
                    new String[]{"_id","data1","data2","data3"}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId , null, null);
            while(emailCur.moveToNext()) {
                int i = emailCur.getInt(0);
                String str = emailCur.getString(1);
                str = emailCur.getString(2);
                str = emailCur.getString(3);
            }
            emailCur.close();

        }
        cursor.close();
    }

    private ArrayList<RowItem> getAllContactsNames() {


        ArrayList<RowItem> listContacts= new ArrayList<>();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()){
            //get name
            int nameFiledColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFiledColumnIndex);
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));

            RowItem rowItem= new RowItem(contact, Integer.valueOf(contactId));

            listContacts.add(rowItem);



        }
        cursor.close();

        return listContacts;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Return of the activity with code to create new contact
        if(resultCode==1){

            CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityMain);
            Snackbar snackbar = Snackbar
                    .make( layout , "Contact Added", Snackbar.LENGTH_LONG);
            snackbar.show();




            itemsContact=mDbContactsHelper.selectAllContacts(db);
            mContactListAdapter= new ContactListAdapter(this,R.layout.contact,itemsContact);
            listViewContacts.setAdapter(mContactListAdapter);

        }

        //Return of the activity to edit a contact
        if(resultCode==2){

            CoordinatorLayout layout= (CoordinatorLayout) findViewById(R.id.activityMain);
            Snackbar snackbar = Snackbar
                    .make( layout , "Contact Updated", Snackbar.LENGTH_LONG);
            snackbar.show();
            mSlidePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            refreshListView();
        }


    }

    //Adds dummy contacts to the database
//    private void addDummyContactsDB(SQLiteDatabase db) {
//
//        ContentValues values = new ContentValues();
//        values.put(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,"Maria" );
//        values.put(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME, "Silva");
//        values.put(ContactsDBContraints.contactEntry.COLUMN_BIRTH, "1996-04-22");
//        values.put(ContactsDBContraints.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsDBContraints.contactEntry.TABLE_NAME, null, values);
//
//
//        values.clear();
//        values.put(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,"Beyonce" );
//        values.put(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME, "Joaquina");
//        values.put(ContactsDBContraints.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsDBContraints.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsDBContraints.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsDBContraints.contactEntry.TABLE_NAME, null, values);
//
//        values.clear();
//        values.put(ContactsDBContraints.contactEntry.COLUMN_NAME_FIRST_NAME,"Chris" );
//        values.put(ContactsDBContraints.contactEntry.COLUMN_LAST_NAME, "Rock");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Dominguinhos" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "do Rojao");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Jack Bauer" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "de Jesus");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Jose" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Dirceu");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Leandro" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Soares");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Lula" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "da Silva");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//
//        values.clear();
//        values.put(ContactsContract.contactEntry.COLUMN_NAME_FIRST_NAME,"Rihana" );
//        values.put(ContactsContract.contactEntry.COLUMN_LAST_NAME, "Umbrella");
//        values.put(ContactsContract.contactEntry.COLUMN_BIRTH, "1997-05-12");
//        values.put(ContactsContract.contactEntry.COLUMN_PHONE,"(12)3921-8503" );
//        values.put(ContactsContract.contactEntry.COLUMN_ZIP_CODE, "02832-000" );
//
//
//        db.insert(ContactsContract.contactEntry.TABLE_NAME, null, values);
//
//
//    }

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


    //Refreshs the list view on update
    private void refreshListView(){

        itemsContact=mDbContactsHelper.selectAllContacts(db);

        mContactListAdapter= new ContactListAdapter(this,R.layout.contact,itemsContact);

        listViewContacts.setAdapter(mContactListAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();

        //checks if it is the first run
        if (prefs.getBoolean("firstrun", true)) {
            //addDummyContactsDB(db);
            refreshListView();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }


    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

}
