package com.example.myapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Record {

    static ArrayList<Record> allRecord = new ArrayList<>();

    public static int num = 1;
    public String id;
    public String email;
    public String password;

    public String main_topic;
    public String sub_topic;
    public int answered;
    public int unattempted;
    public int correct;
    public int marked;


    Record(String _email,String _password){
        this.id = num++ + "";
        this.email = _email;
        this.password = _password;
    }

    Record(String email, String main, String sub, int ans, int unv, int una, int mark){
        this.email = email;
        this.main_topic = main;
        this.sub_topic = sub;
        this.answered = ans;
        this.correct = unv;
        this.unattempted = una;
        this.marked = mark;
    }
}
