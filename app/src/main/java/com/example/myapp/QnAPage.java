package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QnAPage extends AppCompatActivity {

    static boolean check = false;
    private FloatingActionButton float_butn;
    Button one, two, three, four, five, six, seven, eight, nine, ten, prev_button, next_button;
    boolean flag = false, unvis = false;
    public int counter, minutes = 9;
    Button op1, op2, op3, op4;
    Button continue_button;
    ImageView bookmark;
    TextView timer, qno, question;
    ArrayList<Button> op = new ArrayList<>();
    ArrayList<QuestionSet> set = new ArrayList<>();
    ArrayList<Button> ten_buttons = new ArrayList<>();

    AlertDialog.Builder buildr;

    //------------------------Answer storage-------------------------------

    HashMap<Integer, Integer> selected_options = new HashMap<>();
    HashMap<Integer, Button> correct_buttons = new HashMap<>();
    boolean marked[] = {false, false, false, false, false, false, false, false, false, false};
    String sub;

    //.....................................................................
    static int QNO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qn_apage);
        check = false;
        QNO = 0;

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_window);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < 10; i++){
            selected_options.put(i, 0);
        }

        question = findViewById(R.id.question);
        qno = findViewById(R.id.qno);
        bookmark = findViewById(R.id.bookmark);
        float_butn = findViewById(R.id.summary_popup);
        prev_button = findViewById(R.id.prev_button);
        next_button = findViewById(R.id.next_button);
        continue_button = dialog.findViewById(R.id.contine_btn);
        one = (Button) dialog.findViewById(R.id.one);two = (Button) dialog.findViewById(R.id.two);
        three = (Button) dialog.findViewById(R.id.three);four = (Button) dialog.findViewById(R.id.four);
        five =(Button) dialog.findViewById(R.id.five);six = (Button) dialog.findViewById(R.id.six);
        seven = (Button) dialog.findViewById(R.id.seven);eight =(Button) dialog.findViewById(R.id.eight);
        nine = (Button) dialog.findViewById(R.id.nine);ten = (Button) dialog.findViewById(R.id.ten);
        ten_buttons.add(one);ten_buttons.add(two);ten_buttons.add(three);
        ten_buttons.add(four);ten_buttons.add(five);ten_buttons.add(six);
        ten_buttons.add(seven);ten_buttons.add(eight);ten_buttons.add(nine);ten_buttons.add(ten);


        //----------------------------------Getting topic and main toic from previos page=======================
        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences("LoginTracker", MODE_PRIVATE);

        String main_topic = sp.getString("main_topic", "");
        sub = intent.getStringExtra("sub");
        DBHelper temp = new DBHelper(this);
        set = temp.getQuestionSet(main_topic, sub);

        //.....................................................................

        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        timer = findViewById(R.id.timer);
        bookmark = findViewById(R.id.bookmark);
        op.add(op1);
        op.add(op2);
        op.add(op3);
        op.add(op4);
        timeFunction();
        setDefaultColor();
        currentQuestion(QNO);


        float_butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                setDefaultColor2();
                ten_buttons.get(QNO).setBackground(getResources().getDrawable(R.drawable.orange_button));
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag) {
                    view.setBackground(getResources().getDrawable(R.drawable.red_star_svg));
                    flag = true;
                    marked[QNO] = true;
                }
                else{
                    view.setBackground(getResources().getDrawable(R.drawable.star));
                    flag = false;
                    marked[QNO] = false;
                }

            }
        });

    }
   // =====================Crrate ended------------------------------------

    public void popup_button_update(View v){

        setDefaultColor2();
        Button btn = (Button) v;
        v.setBackground(getResources().getDrawable(R.drawable.orange_button));
        int num = Integer.parseInt(btn.getText().toString());
        QNO = num - 1;
        currentQuestion(QNO);
    }

    public void nextQuestion(View v){
        flag = false;
        setDefaultColor();
        if(next_button.getText().toString().equals("submit")){
            finalSubmit("");
        }
        if(QNO == 8)
            next_button.setText("submit");
       if(QNO < 9){
           currentQuestion(++QNO);
       }
       if(selected_options.get(QNO) != 0){
           correct_buttons.get(QNO).setBackgroundColor(getResources().getColor(R.color.green));
       }
    }

    public void prevQuestion(View v){
        flag = false;
        setDefaultColor();
        if(QNO < 10)
            next_button.setText("next");
        if(QNO >= 1){
            currentQuestion(--QNO);
        }
        if(selected_options.get(QNO) != 0){
            correct_buttons.get(QNO).setBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    public void currentQuestion(int n){
        if(QNO == 9)
            next_button.setText("submit");

        qno.setText((QNO + 1) + "");
        question.setText(set.get(n).question);
        op1.setText(set.get(n).option1);
        op2.setText(set.get(n).option2);
        op3.setText(set.get(n).option3);
        op4.setText(set.get(n).option4);

        setDefaultColor();
        if(selected_options.get(QNO) != 0) {
            correct_buttons.get(QNO).setBackgroundColor(getResources().getColor(R.color.green));
        }

        if(!marked[QNO]){
            bookmark.setBackground(getResources().getDrawable(R.drawable.star));
        }
        else{
            bookmark.setBackground(getResources().getDrawable(R.drawable.red_star_svg));
        }
    }




    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Connot exit the test", Toast.LENGTH_SHORT).show();
    }


//    ---------------------------------------Timer Function----------------------------------------------

    private void timeFunction() {
        if (minutes > -1){
            if(minutes < 2){
                timer.setTextColor(getResources().getColor(R.color.red));
            }
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String min = "";
                    long seconds = (millisUntilFinished / 1000);
                    if(minutes < 10){
                        min =  "0" + minutes;
                    }
                    String secs = "" + seconds;
                    if(seconds < 10)
                        secs = "0" + seconds;

                    timer.setText(min + " : " + secs);
                }

                public void onFinish() {
                    minutes--;
                    timeFunction();
                }
            }.start();
        }
        else{
            if(!check)
                finalSubmit("time out");
        }
    }

    //    -------------------------------------------------------------------------------------------------

    //On click of options
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void option_selected(View v){
        Button current_selected = (Button) v;
        int num = 0;
        switch(current_selected.getId()){
            case R.id.op1 : num = 1; break;
            case R.id.op2 : num = 2; break;
            case R.id.op3 : num = 3; break;
            case R.id.op4 : num = 4; break;
        }
        setDefaultColor();
        current_selected.setBackgroundColor(getResources().getColor(R.color.green));
        correct_buttons.put(QNO, current_selected);
        selected_options.replace(QNO, num);
    }



    private void setDefaultColor() {
        for(Button temp : op)
            temp.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setDefaultColor2() {
        for(int i = 0; i < 10; i++){
            if(marked[i])
                ten_buttons.get(i).setBackground(getResources().getDrawable(R.drawable.red_button));
            else if(selected_options.get(i) == 0)
                ten_buttons.get(i).setBackground(getResources().getDrawable(R.drawable.white_button));
            else if(selected_options.get(i) > 0){
                ten_buttons.get(i).setBackground(getResources().getDrawable(R.drawable.green_button));
            }
        }
    }

    public void finalSubmit(String status){

        if(status.equals("time out")){
            finalSubmit2();
            return;
        }

        buildr = new AlertDialog.Builder(this);
        buildr.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int x) {
                        dialogInterface.cancel();
                        finalSubmit2();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = buildr.create();
        alert.setTitle("Submit the test!!");
        alert.show();


    }

    public void finalSubmit2(){
        check = true;
        minutes = -1;
        Intent intent = new Intent(this, Result.class);
        DBHelper helper = new DBHelper(getApplicationContext());

        int answered = 0, bookmarked = 0, unattempted = 0, unvisited = 0;
        for(int i = 0; i < 10; i++){
            if(marked[i])
                bookmarked++;
            if(selected_options.get(i) != 0)
                answered++;
            if(selected_options.get(i) == 0)
                unattempted++;
        }

        ArrayList<Integer> arr = new ArrayList<>();
        int correct_res = 0;
        for(int i = 0; i < 10; i++){
            int val = -1;
            if(set.get(i).correct_option.equals(set.get(i).option1))
                val = 1;
            if(set.get(i).correct_option.equals(set.get(i).option2))
                val = 2;
            if(set.get(i).correct_option.equals(set.get(i).option3))
                val = 3;
            if(set.get(i).correct_option.equals(set.get(i).option4))
                val = 4;
            arr.add(val);

            if(arr.get(i) == selected_options.get(i))
                correct_res++;
        }

        Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("LoginTracker", MODE_PRIVATE);
        String id = sp.getString("id", "");
        String main_topic = sp.getString("main_topic", "");

        Record tempRec = new Record(id, main_topic, sub, answered, correct_res, unattempted, bookmarked);
        DBHelper putRes = new DBHelper(getApplicationContext());
        long res = putRes.addResult(tempRec);



        intent.putExtra("main", main_topic);
        intent.putExtra("sub", sub);
        intent.putExtra("answered", answered);
        intent.putExtra("unattempted", unattempted);
        intent.putExtra("marked", bookmarked);
        intent.putExtra("correct", correct_res);
        startActivity(intent);
    }


}