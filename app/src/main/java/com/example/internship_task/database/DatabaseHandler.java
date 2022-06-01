package com.example.internship_task.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.internship_task.model.RequestModel;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RequestManager";
    private static final String TABLE = "Requests";
    private static final String KEY_ID = "id";
    private static final String KEY_Method = "Method";
    private static final String KEY_Url = "Url";
    private static final String KEY_Response = "Response";
    private static final String KEY_ResponseHeaders = "ResponseHeaders";
    private static final String KEY_RequestHeaders = "RequestHeaders";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" + KEY_ID + " Integer PRIMARY KEY AUTOINCREMENT,"
                + KEY_Method + " TEXT," + KEY_Url + " TEXT," + KEY_Response + " TEXT NOT NULL," + KEY_ResponseHeaders + " TEXT," + KEY_RequestHeaders + " TEXT" + " )";
        db.execSQL(sql);

    }

    public void addRequest(RequestModel ob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Method, ob.getMethod());
        values.put(KEY_Url, ob.getUrl());
        values.put(KEY_Response, ob.getResponse());
        values.put(KEY_ResponseHeaders, ob.getResponseHeaders());
        values.put(KEY_RequestHeaders, ob.getResponseHeaders());
        db.insert(TABLE, null, values);
    }

    public ArrayList<RequestModel> getAllRequests() {
        ArrayList<RequestModel> requestList = new ArrayList<RequestModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT  * FROM " + TABLE;
        Cursor query = db.rawQuery(sql, null);
        while (query.moveToNext()) {
            RequestModel contact = new RequestModel(Integer.parseInt(query.getString(0)), query.getString(1), query.getString(2),
                    query.getString(3), query.getString(4),query.getString(5));
            requestList.add(contact);
        }
        query.close();
        return requestList;

    }    public ArrayList<RequestModel> sortByMethod(String method) {
        ArrayList<RequestModel> requestList = new ArrayList<RequestModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT  * FROM " + TABLE;
        Cursor query = db.rawQuery(sql, null);
        while (query.moveToNext()) {
            if ( query.getString(1).equals(method)){
            RequestModel contact = new RequestModel(Integer.parseInt(query.getString(0)), query.getString(1), query.getString(2),
                    query.getString(3), query.getString(4), query.getString(5));
            requestList.add(contact);}
        }
        query.close();
        return requestList;
    }



    public RequestModel getRequest(String url) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[]{KEY_ID, KEY_Method, KEY_Url, KEY_Response, KEY_ResponseHeaders,KEY_RequestHeaders}, KEY_Url + "=?",
                new String[]{url}, null, null, null, null);
        RequestModel request = null;
        if (cursor.moveToFirst()) {
            request = new RequestModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        }
        cursor.close();

        return request;
    }

    public void delete() {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.execSQL("DELETE FROM "+TABLE);
        db.close();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }
}
