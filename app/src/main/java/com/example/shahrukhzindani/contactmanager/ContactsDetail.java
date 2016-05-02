//Created by Shahrukh Zindani
package com.example.shahrukhzindani.contactmanager;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahrukhzindani.contactmanager.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds;

/**
 * Created by shahrukhzindani on 4/18/16.
 */
public class ContactsDetail extends Activity {

    Contact testContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String phoneNumber = null;
        String email = null;
        String address = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String _ID = ContactsContract.Contacts._ID;

        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;


        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String Address_ID = CommonDataKinds.StructuredPostal.CONTACT_ID;

        String DATA = ContactsContract.CommonDataKinds.Email.DATA;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        ListView l;
        l = (ListView) findViewById(R.id.listView);
        String name = getIntent().getExtras().getString("username");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //StringBuffer output = new StringBuffer();


        List<String> contacts = new ArrayList<String>();
        testContact = new Contact();
        while (c.moveToNext()) {
            // Saving all the fields like name, address, email number etc in the Contacts object.
            //I am passing all the fields for a specific contact in and setting them to the object.
            if (name.equals(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {
                String contact_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                testContact.setId(contact_id);
                if (c.getCount() > 0) {

                    int hasPhoneNumber = Integer.parseInt(c.getString(c.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        contacts.add("\n Name: " + name);
                        testContact.setName(name);


                        Cursor phoneCursor = cr.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (phoneCursor.moveToNext()) {

                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                            contacts.add("\n Phone number: " + phoneNumber);
                            testContact.setPhoneNumber(phoneNumber);

                        }

                        phoneCursor.close();


                        Cursor emailCursor = cr.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (emailCursor.moveToNext()) {

                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            if (email.length() > 0) {
                                Log.d("Email Cursor", "REACHED PHONE NUMBER");
                                if (!contacts.contains("\nEmail: " + email)) {
                                    contacts.add("\nEmail: " + email);
                                    testContact.setEmail(email);
                                }

                            }
                        }


                        emailCursor.close();


                        //contacts.add(c.getString(c.getColumnIndex(ContactsContract.Contacts.)));

                        Cursor addressCursor = cr.query(CommonDataKinds.StructuredPostal.CONTENT_URI, null, Address_ID + "= ?", new String[]{contact_id}, null);
                        while (addressCursor.moveToNext()) {

                            address = addressCursor.getString(addressCursor.getColumnIndex(CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                            if (address.length() > 0) {
                                Log.d("Email Cursor", "REACHED PHONE NUMBER");
                                contacts.add("\nAddress: " + address);
                                testContact.setPostalAddress(address);
                            }
                        }


                        addressCursor.close();

                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            l.setAdapter(adapter);




        }



    }

    public void mapClick(View view) {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=+" + testContact.getPostalAddress());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent); // fires an intent and takes the user to the googke maps function.
    }
    public void callClick(View view) {// It calls the number by parsing the number from the phone number field

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + testContact.getPhoneNumber()));
        startActivity(callIntent);
    }

    public void deleteClick(View view) { // Takes id of the specific contact and deletes it from the content provider
        ContentResolver cr = getContentResolver();
        String where = ContactsContract.Contacts.Data._ID + "= ?";
        String [] params = new String [] {testContact.getId()};
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(where,params)
                .build());
        Intent intent = new Intent(this,ContactsDisplay.class);
        startActivity(intent);
        try{
            cr.applyBatch(ContactsContract.AUTHORITY,ops);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(OperationApplicationException e){
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Contacts Deleted", Toast.LENGTH_SHORT).show();

    }
    public void editClick(View view){ // Edit Click basically takes the specific contact data and deletes it. It passes the data to the next activity.

        ContentResolver cr = getContentResolver();
        String where = ContactsContract.Contacts.Data._ID + "= ?";
        String [] params = new String [] {testContact.getId()};
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(where,params)
                .build());
        Intent intent = new Intent(this,ContactsDisplay.class);
        startActivity(intent);
        try{
            cr.applyBatch(ContactsContract.AUTHORITY,ops);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(OperationApplicationException e){
            e.printStackTrace();
        }

        Intent intent1 = new Intent(this,EditContact.class);
        intent1.putExtra("id",testContact.getId());
        intent1.putExtra("name",testContact.getName());
        intent1.putExtra("number",testContact.getPhoneNumber());
        intent1.putExtra("email",testContact.getEmail());
        intent1.putExtra("address",testContact.getPostalAddress());


        startActivity(intent1);
    }




}

