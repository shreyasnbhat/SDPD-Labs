package com.example.shreyas.locationshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_RESULT = 123;
    private String[] permissionList = {Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    private DBHandler dbHandler;

    private RecyclerView trustedContactRecyclerView;
    private ArrayList<Contact> contactList = new ArrayList<>();
    private TrustedContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAllPermissions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trustedContactRecyclerView = findViewById(R.id.recycler_view);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact(view);
            }
        });

        dbHandler = new DBHandler(this);

        // Recycler View Stuff
        adapter = new TrustedContactAdapter(contactList, this,dbHandler);
        trustedContactRecyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        trustedContactRecyclerView.setLayoutManager(manager);
        trustedContactRecyclerView.setHasFixedSize(true);

        contactList.clear();
        contactList.addAll(dbHandler.getAllContacts());
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    getContactData(data);
                    break;
            }
        } else {
            Log.e("TAG", "Failed to get contact!");
        }
    }

    private void requestAllPermissions() {
        PermissionManager.grantAllPermissions(MainActivity.this, permissionList);
    }

    public void pickContact(View v) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    public void getContactData(Intent data) {

        Cursor cursor;
        try {
            String contactNumber;
            String contactName;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int contactNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int contactNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            contactNumber = cursor.getString(contactNumberIndex);
            contactName = cursor.getString(contactNameIndex);
            addContactToTrustedList(contactName, ContactFormat.format(contactNumber));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void addContactToTrustedList(String contactName, String contactNumber) {
        dbHandler.addContact(new Contact(contactName, contactNumber));
        List<Contact> contactListFromDB = dbHandler.getAllContacts();

        // Populate Trusted Contacts in the Home screen
        contactList.clear();
        contactList.addAll(contactListFromDB);
        adapter.notifyDataSetChanged();
    }


}
