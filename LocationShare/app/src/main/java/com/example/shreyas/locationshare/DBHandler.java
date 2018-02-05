package com.example.shreyas.locationshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.shreyas.locationshare.ContactEntry.CONTACTS_KEY_NAME;
import static com.example.shreyas.locationshare.ContactEntry.CONTACTS_KEY_NUMBER;
import static com.example.shreyas.locationshare.ContactEntry.CONTACTS_TABLE_NAME;

/**
 * Created by shreyas on 4/2/18.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Constants for Tables
    private final static int DB_VERSION = 1;
    private final static String DATABASE_NAME = "trusted.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE_NAME + "(" +
                CONTACTS_KEY_NUMBER + " TEXT PRIMARY KEY," +
                CONTACTS_KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME;
        db.execSQL(DROP_TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contents = new ContentValues();
        Log.e("Contact", contact.toString());
        contents.put(CONTACTS_KEY_NUMBER, contact.getNumber());
        contents.put(CONTACTS_KEY_NAME, contact.getName());

        try {
            db.insert(CONTACTS_TABLE_NAME, null, contents);
            db.close();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }

    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        String SELECT_ALL_QUERY = "SELECT * FROM " + CONTACTS_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setNumber(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contactList;
    }

    public Contact getContactById(String number) {
        Contact contact = new Contact();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CONTACTS_TABLE_NAME, new String[]{CONTACTS_KEY_NUMBER,
                        CONTACTS_KEY_NAME}, CONTACTS_KEY_NUMBER + "=?",
                new String[]{String.valueOf(number)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        try {
            contact.setNumber(cursor.getString(0));
            contact.setName(cursor.getString(1));
        } catch (CursorIndexOutOfBoundsException e) {
            contact = null;
        }

        cursor.close();
        return contact;
    }

    public void deleteContactById(String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CONTACTS_TABLE_NAME, CONTACTS_KEY_NUMBER + " = ?",
                new String[]{number});
        db.close();
    }
}
