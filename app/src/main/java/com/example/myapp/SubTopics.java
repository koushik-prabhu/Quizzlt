package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SubTopics extends AppCompatActivity {
    ListView listView;
    EditText sv;
    ArrayList<String> arr  =  new ArrayList<>();
    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sub_topics);
        listView = (ListView) findViewById(R.id.listView1);
        sv = (EditText) findViewById(R.id.search_me);

        //Fetching sent subject from home page
        Intent intent = getIntent();
        String subject = intent.getStringExtra("subject");
        SharedPreferences sp = getSharedPreferences("LoginTracker", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();
        String email = sp.getString("email", "");
        myEdit.putString("main_topic", subject);
        myEdit.apply();

        //Fetch from subjects class

        Subjects sub = new Subjects();
        arr = (ArrayList<String>) sub.getSubTopics(subject);

        DBHelper helper = new DBHelper(this);
        ArrayList<String> tempList = new ArrayList<>();
        tempList = helper.getSubList(email, subject);

        for(int i = 0; i < tempList.size(); i++){
            arr.remove(tempList.get(i));
        }

        //custom adapter
        adapter = new CustomAdapter(getApplicationContext(), arr);
        listView.setAdapter(adapter);

        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ArrayList<String> tmp = new ArrayList<>();
                String s = charSequence.toString();
                s = s.toLowerCase();

                for(int x = 0; x < arr.size(); x++){
                    if((arr.get(x).toLowerCase()).startsWith(s)){
                        tmp.add(arr.get(x));
                    }
                }
                adapter = new CustomAdapter(getApplicationContext(), tmp);
                listView.setTextFilterEnabled(true);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void callme(View v){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        ViewGroup grp = (ViewGroup)v.getParent();
//        TextView txt = (TextView) grp.getChildAt(0);
//        Toast.makeText(this, "" + txt.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}