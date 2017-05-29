package com.example.leandrosoares.democontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    private TextView tvLastName;
    private TextView tvPhoneNumber;
    private TextView tvZipCode;
    private TextView tvDateOfBirth;

    private ImageView backgroundProfile;
    private ImageView nameIcon;
    private ImageView phoneIcon;
    private ImageView zipCodeIcon;
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
        tvLastName= (TextView) findViewById(R.id.tvLastName);
        tvPhoneNumber= (TextView) findViewById(R.id.tvPhoneNumber);
        tvZipCode= (TextView) findViewById(R.id.tvZipCode);
        tvDateOfBirth= (TextView) findViewById(R.id.tvDateBirth);


        backgroundProfile=(ImageView) findViewById(R.id.backgroundProfile);
        nameIcon=(ImageView) findViewById(R.id.name_icon);
        starIcon=(ImageView) findViewById(R.id.iconStar);
        zipCodeIcon=(ImageView) findViewById(R.id.iconZipCode);
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

                contact= mDbContactsHelper.selectContact(db,rowID);

                //Fill the fields of the profile panel
                tvFirstName.setText(contact.getFirstName());
                tvLastName.setText(contact.getLastName());
                tvPhoneNumber.setText(contact.getPhoneNumber());
                tvDateOfBirth.setText(contact.getDateOfBirth());
                tvZipCode.setText(contact.getZipCode());

                byte[] photoData=contact.getPhotoByteArray();


                if(photoData!=null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
                    backgroundProfile.setImageBitmap(bitmap);
                }


                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

                // generate color based on a key (same key returns the same color), useful for list/grid views
                int color = generator.getColor(contact.getFirstName()+" "+ contact.getLastName() );

                //Sets the color of the icons to the color generated
                backgroundProfile.setBackgroundColor(color);
                nameIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                phoneIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                starIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                zipCodeIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

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

                intent.putExtra("firstName",contact.getFirstName());
                intent.putExtra("lastName",contact.getLastName());
                intent.putExtra("birthday",contact.getDateOfBirth());
                intent.putExtra("phone",contact.getPhoneNumber());
                intent.putExtra("zipcode",contact.getZipCode());
                intent.putExtra("photo",contact.getPhotoByteArray());
                intent.putExtra("id", rowID);


                startActivityForResult(intent,CONTACT_EDIT);

            }
        });


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
            addDummyContactsDB(db);
            refreshListView();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

}
