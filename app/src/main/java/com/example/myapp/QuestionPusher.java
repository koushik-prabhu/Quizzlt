package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QuestionPusher extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizzltDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "question_sets";
    private static final String main_topic = "main_topic";
    private static final String sub_topic = "sub_topic";
    private static final String question = "question";
    private static final String option1 = "option1";
    private static final String option2 = "option2";
    private static final String option3 = "option3";
    private static final String option4 = "option4";
    private static final String correct_option = "correct_option";

    public QuestionPusher(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String str = "CREATE TABLE " + TABLE_NAME + "(" + main_topic + " TEXT, " + sub_topic + " TEXT, " + question + " TEXT, " + option1 + " TEXT, " + option2 + " TEXT, " + option3 + " TEXT, " + option4 + " TEXT, " + correct_option + " TEXT)";

        //String str = "CREATE TABLE LOL(name TEXT)";
        sqLiteDatabase.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void pushRecords(ArrayList<QuestionSet> list){
        SQLiteDatabase db = getWritableDatabase();

        for(int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(main_topic, list.get(i).main_topic);
            values.put(sub_topic, list.get(i).sub_topic);
            values.put(question, list.get(i).question);
            values.put(option1, list.get(i).option1);
            values.put(option2, list.get(i).option2);
            values.put(option3, list.get(i).option3);
            values.put(option4, list.get(i).option4);
            values.put(correct_option, list.get(i).correct_option);

            long res = db.insert(TABLE_NAME, null, values);
        }
    }


}
