package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;

public class Subjects {

    ArrayList<String> DBMS = new ArrayList<>(Arrays.asList("ER Model", "Fun-Dep", "Relation Algebra", "Relational Model", "Normalization"));
    ArrayList<String> ML = new ArrayList<>(Arrays.asList("Regression", "Svm", "K-Means", "Naive Bayes", "AI"));
    ArrayList<String> JAVA = new ArrayList<>(Arrays.asList("Arrays", "Strings", "Collections", "Inheritance", "Classes"));
    ArrayList<String> DSA = new ArrayList<>(Arrays.asList("Stacks", "Linked List", "Queue", "Binary Tree", "Hash Tables"));
    public ArrayList<String> getSubTopics(String topic){
        switch(topic){
            case "JAVA" : return JAVA;
            case "DSA" : return DSA;
            case "ML" : return ML;
            case "DBMS" : return DBMS;
        }
        return JAVA;
    }
}
