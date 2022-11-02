package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    AlertDialog.Builder builder;
    Button save_profile;
    Dialog dialog;
    TextView name, contact_mail, phone;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        save_profile = (Button) dialog.findViewById(R.id.save_profile);
        name = (TextView) dialog.findViewById(R.id.name);
        contact_mail = dialog.findViewById(R.id.email);
        phone = dialog.findViewById(R.id.phone);
        textView = dialog.findViewById(R.id.textView);

        SharedPreferences sh = getSharedPreferences("LoginTracker", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        boolean theme = sh.getBoolean("theme", false);
        if(theme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        String temp_email = sh.getString("email", "");
        if(temp_email.equals("")){
            Intent i = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(i);
        }

//-------------------------------------------DRAWER THINGS------------------------------------------------
        navigationView= findViewById(R.id.my_view);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//------------------------------------------------------------------------------------------------------------

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = name.getText().toString();
                String _contact_mail = contact_mail.getText().toString();
                String _phone = phone.getText().toString();

                DBHelper helper = new DBHelper(getApplicationContext());
                if(!temp_email.equals("")){
                    helper.updateProfile(temp_email, _name, _contact_mail, _phone);
                }

                dialog.dismiss();
            }
        });
    }

    //Redirect to sub topics activity
    public void sub_topics(View v){
        Button btn = (Button)v;
        Intent intent = new Intent(getApplicationContext(), SubTopics.class);
        intent.putExtra("subject", btn.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String str = (String) item.getTitle();
        if(str.equals("Profile")){
            dialog.show();

            DBHelper helper = new DBHelper(getApplicationContext());
            SharedPreferences sh = getSharedPreferences("LoginTracker", MODE_PRIVATE);

            String mail = sh.getString("email", "");
            ArrayList<String> arr = helper.getProfile(mail);

            if(!arr.get(0).equals("NULL")) {
                name.setText(arr.get(0));
                String profiler = "";
                String[] temp = arr.get(0).split(" ");
                for(String s : temp)
                    profiler += s.charAt(0);

                textView.setText(profiler.toUpperCase());
            }
            if(!arr.get(1).equals("NULL"))
                contact_mail.setText(arr.get(1));
            if(!arr.get(2).equals("NULL"))
                phone.setText(arr.get(2));

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if(str.equals("EXIT")){
            MainActivity.this.finishAffinity();
            System.exit(0);
            return true;
        }
        SharedPreferences sh = getSharedPreferences("LoginTracker", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        if(str.equals("light mode")) {
                myEdit.putBoolean("theme", false);
                myEdit.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        }
        if(str.equals("dark mode")){
                myEdit.putBoolean("theme", true);
                myEdit.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        }

        if(str.equals("RESET")){
            builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DBHelper helper = new DBHelper(getApplicationContext());
                            SharedPreferences sh = getSharedPreferences("LoginTracker", MODE_PRIVATE);
                            String mail = sh.getString("email", "");
                            helper.deleteAll(mail);
                            Toast.makeText(MainActivity.this, "data erased!", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("All the data will be erased!!");
            alert.show();
        }

        if(str.equals("HISTORY")){
            Intent intent = new Intent(getApplicationContext(), OverallSummary.class);
            startActivity(intent);
            return true;
        }

        if(str.equals("LOG OUT")){
            SharedPreferences sp = getSharedPreferences("LoginTracker", MODE_PRIVATE);
            myEdit.putString("email", "");
            myEdit.putString("password", "");
            myEdit.apply();
            Intent i = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(i);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        MainActivity.this.finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Exit the app");
        alert.show();
    }
}