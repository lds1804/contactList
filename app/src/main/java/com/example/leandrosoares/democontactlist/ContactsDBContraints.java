package com.example.leandrosoares.democontactlist;

import android.provider.BaseColumns;

/**
 * Created by leandrosoares on 24/05/17.
 * Class to store table name and columns of the table contacts
 */
public final class ContactsDBContraints {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ContactsDBContraints() {}

    /* Inner class that defines the table contents */
    public static class contactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_BIRTH = "date_of_birth";
        public static final String COLUMN_ZIP_CODE = "zip_code";
        public static final String COLUMN_PHOTO = "photo";





    }
}

