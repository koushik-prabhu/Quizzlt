package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    EditText email, password;
    Button submit;
    TextView error_msg;
    ArrayList<QuestionSet> list = new ArrayList<QuestionSet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        DBHelper temp = new DBHelper(getApplicationContext());
        DBHelper db = new DBHelper(getApplicationContext());

//---------------------------------------------------LOAD DATABASE----------------------------------------------------

//        ArrayList<Record> LoginData = new ArrayList<>();
//        LoginData.add(new Record("abcd@gmail.com", "abcd"));
//        LoginData.add(new Record("kp@gmail.com", "abcd"));
//
//        for(Record temp1 : LoginData)
//            db.addRecord(temp1);
//
//        get_json();
// -------------------------------------------------------------------------------------------------------------------

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        submit = (Button) findViewById(R.id.login_submit);
        error_msg = findViewById(R.id.error_msg);

        //Login button submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _email = email.getText().toString();
                String _password = password.getText().toString();

                if(checkNull(_email, _password)){
                    //Check login credentials in database (if exists returns true)
                    if(db.getLoginData(_email, _password)){
                        setPereference(_email, _password);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else
                        error_msg.setText("No record found!");
                }
            }
            //Set sharepreference if user logs in (to create a session)
            public void setPereference(String email, String password){
                SharedPreferences sharedPreferences = getSharedPreferences("LoginTracker", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString("id", email);
                myEdit.putString("email", email);
                myEdit.putString("password", password);
                myEdit.apply();
            }

            //To check if user credentials are null or not
            private boolean checkNull(String _email, String _password) {

                if(_email.equals("") && _password.equals("")){
                    error_msg.setText("Please enter credentials!");
                    return false;
                }
                if(_email.equals("")){
                    error_msg.setText("Please enter your email!");
                    return false;
                }
                if(_password.equals("")){
                    error_msg.setText("Please enter passowrd!");
                    return false;
                }
                return true;
            }
        });
    }

    //Load json to databse
    public void get_json(){
        String json;

        try{
            InputStream is = getAssets().open("dataset.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            DBHelper t = new DBHelper(getApplicationContext());
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String main = obj.getString("main_topic");
                String sub = obj.getString("sub_topic");
                String ques = obj.getString("question");
                String op1 = obj.getString("option1");
                String op2 = obj.getString("option2");
                String op3 = obj.getString("option3");
                String op4 = obj.getString("option4");
                String correct = obj.getString("correct_option");

                list.add(new QuestionSet(main, sub, ques, op1, op2, op3, op4, correct));
            }
            t.pushRecords(list);
        }
        catch (IOException e){
            Toast.makeText(this, "IO", Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e){
            Toast.makeText(this, "JSONN", Toast.LENGTH_SHORT).show();
        }
    }
}