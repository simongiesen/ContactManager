// Author name : Shahrukh Zindani
package com.example.shahrukhzindani.contactmanager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.shahrukhzindani.contactmanager.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.*;
import static android.provider.ContactsContract.CommonDataKinds.*;

public class ContactsDisplay extends Activity implements OnItemClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_display);
        ListView l;
        l = (ListView) findViewById(R.id.listView);
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] {Contacts.DISPLAY_NAME},null,null,null);

// This whole method basically takes the contacts from the content provider and just focuses on DISPLAY_NAME.
// I save that in a list of strings and then display the list as a list view.

    List<String> contacts = new ArrayList<String>();
    if(c.moveToFirst()){
        do{
            contacts.add(c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME)));
            Log.d("my tag", "contacts added");
        }
        while (c.moveToNext());
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contacts);
    l.setAdapter(adapter);
    l.setOnItemClickListener(this);
}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView temp = (TextView) view;
        Intent intent = new Intent(ContactsDisplay.this,ContactsDetail.class);
        intent.putExtra("username",temp.getText().toString());
        startActivity(intent);
        //Click on a specific contact and it takes you to the detail contact page
    }
}