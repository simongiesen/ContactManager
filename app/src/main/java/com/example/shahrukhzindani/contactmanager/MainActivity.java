//Author Shahrukh Zindani
package com.example.shahrukhzindani.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String [] options= {"Add a Contact", "Dial a number"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,options);
        l.setAdapter(adapter);
        l.setOnItemClickListener(this);
    }
// The main view is a list view giving two options.
// One is Add a Contact and the other Dial a Contact
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i){
            case 0:
                Intent intent = new Intent(this,AddContactActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                startActivity(intent1);
        }
    }

    public void imageClick(View view) {
        Intent intent = new Intent(this,ContactsDisplay.class);
        startActivity(intent);

    }




}
