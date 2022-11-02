package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class OverallSummary extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> list = new ArrayList();
    ArrayList<Record> data = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_summary);

        SharedPreferences sh = getSharedPreferences("LoginTracker", MODE_PRIVATE);
        String mail = sh.getString("email", "");

        DBHelper helper = new DBHelper(this);
        data = helper.getResult(mail);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        for(int i = 0; i < data.size(); i++){
            String str = data.get(i).sub_topic + "  [" + data.get(i).main_topic + "]\t\t\t\t\t\t" + "Score : " + data.get(i).correct + " / 10";
            list.add(str);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, Result.class);

        intent.putExtra("main", data.get(i).main_topic);
        intent.putExtra("sub", data.get(i).sub_topic);
        intent.putExtra("answered", data.get(i).answered);
        intent.putExtra("unattempted", data.get(i).unattempted);
        intent.putExtra("marked", data.get(i).marked);
        intent.putExtra("correct", data.get(i).correct);
        startActivity(intent);
    }
}