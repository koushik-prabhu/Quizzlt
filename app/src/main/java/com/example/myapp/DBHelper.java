package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuizzltDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "login_info";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TABLE_NAME2 = "question_sets";
    private static final String TABLE_NAME3 = "result_set";
    private static final String TABLE_NAME4 = "profile_set";
    private static final String main_topic = "main_topic";
    private static final String sub_topic = "sub_topic";
    private static final String question = "question";
    private static final String option1 = "option1";
    private static final String option2 = "option2";
    private static final String option3 = "option3";
    private static final String option4 = "option4";
    private static  final String answered = "answered";
    private static final String correct = "correct";
    private static final String marked = "marked";
    private static final String unattempted = "unattempted";
    private static final String correct_option = "correct_option";
    private static final String NAME = "name";
    private static final String CONTACT_EMAIL = "contact_email";
    private static final String PHONE = "phone";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String str1 = "CREATE TABLE " + TABLE_NAME + "(" + ID + " TEXT, " + EMAIL + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT)";
        DB.execSQL(str1);
        String str2 = "CREATE TABLE " + TABLE_NAME2 + "(" + main_topic + " TEXT, " + sub_topic + " TEXT, " + question + " TEXT, " + option1 + " TEXT, " + option2 + " TEXT, " + option3 + " TEXT, " + option4 + " TEXT, " + correct_option + " TEXT)";
        DB.execSQL(str2);
        String str3 = "CREATE TABLE " + TABLE_NAME3+ "(" + EMAIL + " TEXT REFERENCES " + TABLE_NAME + "(" + EMAIL + ")," + main_topic + " TEXT, " + sub_topic + " TEXT, " + answered + " INTEGER, " + unattempted + " INTEGER, " + marked + " INTEGER, " + correct + " INTEGER)";
        DB.execSQL(str3);
        String str4 = "CREATE TABLE " + TABLE_NAME4+ "(" + ID + " TEXT, " + NAME + " TEXT, " + CONTACT_EMAIL + " TEXT, " + PHONE + " TEXT)";
        DB.execSQL(str4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(DB);
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

            long res = db.insert(TABLE_NAME2, null, values);
        }
    }

    public long addRecord(Record temp){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID, temp.id);
        values.put(EMAIL, temp.email);
        values.put(PASSWORD, temp.password);

        long res = db.insert(TABLE_NAME, null, values);

        ContentValues v = new ContentValues();
        v.put(ID, temp.email);
        v.put(NAME, "NULL");
        v.put(CONTACT_EMAIL, temp.email);
        v.put(PHONE, "NULL");
        db.insert(TABLE_NAME4, null, v);

        return res;
    }

    public long addResult(Record temp){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EMAIL, temp.email);
        values.put(main_topic, temp.main_topic);
        values.put(sub_topic, temp.sub_topic);
        values.put(answered, temp.answered);
        values.put(unattempted, temp.unattempted);
        values.put(marked, temp.marked);
        values.put(correct, temp.correct);

        long res = db.insert(TABLE_NAME3, null, values);

        return res;
    }

    public boolean getLoginData(String _email, String _password){

        ArrayList<Record> list = new ArrayList<Record>();
        String str = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL + " = '"+_email+"' AND " + PASSWORD + " = '"+_password+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(str, null);

        if(cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public ArrayList<String> getSubList(String email, String topic){

        ArrayList<String> list = new ArrayList<String>();
        String str = "SELECT " + sub_topic + " FROM " + TABLE_NAME3 + " WHERE " + EMAIL + " = '"+email+"' AND " + main_topic + " = '"+topic+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(str, null);

        if(cursor.moveToFirst()){
            do{
                String t1 = cursor.getString(0);
                list.add(t1);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void deleteAll(String email){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME3, EMAIL + " = ? ", new String[]{email});
    }

    public ArrayList<QuestionSet> getQuestionSet(String main, String sub){
        ArrayList<QuestionSet> temp = new ArrayList<>();
        String str = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + main_topic + " = '"+main+"' AND " + sub_topic + " = '"+sub+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(str, null);

        if(cursor.moveToFirst()){
            do{
                String t1 = cursor.getString(0);
                String t2 = cursor.getString(1);
                String t3 = cursor.getString(2);
                String t4 = cursor.getString(3);
                String t5 = cursor.getString(4);
                String t6 = cursor.getString(5);
                String t7 = cursor.getString(6);
                String t8 = cursor.getString(7);

                temp.add(new QuestionSet(t1, t2, t3, t4, t5, t6, t7, t8));
            }while(cursor.moveToNext());
        }
        return temp;
    }

    public ArrayList<Record> getResult(String _email){
        ArrayList<Record> rec = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + EMAIL + " = '"+_email+"'";
        Cursor cursor = db.rawQuery(str, null);
        if(cursor.moveToFirst()){
            do{
                String t1 = cursor.getString(0);
                String t2 = cursor.getString(1);
                String t3 = cursor.getString(2);
                int t4 = cursor.getInt(3);
                int t5 = cursor.getInt(6);
                int t6 = cursor.getInt(4);
                int t7 = cursor.getInt(5);

                rec.add(new Record(t1, t2, t3, t4, t5, t6, t7));
            }while(cursor.moveToNext());
        }

        return rec;
    }

    public void updateProfile(String email, String name, String contact_mail, String phone){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CONTACT_EMAIL, contact_mail);
        values.put(PHONE, phone);
        String whereClause = ID + "=?";
        String whereArgs[] = {email};
        db.update(TABLE_NAME4, values, whereClause, whereArgs);
    }

    public ArrayList<String> getProfile(String email){
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str = "SELECT * FROM " + TABLE_NAME4 + " WHERE " + ID + " = '"+email+"'";
        Cursor cursor = db.rawQuery(str, null);
        if(cursor.moveToFirst()){
            do{
                arr.add(cursor.getString(1));
                arr.add(cursor.getString(2));
                arr.add(cursor.getString(3));
            }while(cursor.moveToNext());
        }
        return arr;
    }
}
