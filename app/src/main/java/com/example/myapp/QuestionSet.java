package com.example.myapp;

public class QuestionSet {
    String main_topic, sub_topic, question, option1, option2, option3, option4, correct_option;

    public QuestionSet(){}
    public QuestionSet(String a, String b, String c, String d, String e, String f, String g, String h){
        this.main_topic = a;
        this.sub_topic = b;
        this.question = c;
        this.option1 = d;
        this.option2 = e;
        this.option3 = f;
        this.option4 = g;
        this.correct_option = h;
    }
}
