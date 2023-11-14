package com.example.assignment1;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.assignment1.Constants.BMI;
import static com.example.assignment1.Constants.STANDARD;
import static com.example.assignment1.Constants.TABLE_NAME;
import static com.example.assignment1.Constants.DATE;
import static com.example.assignment1.Constants.WEIGHT;


public class EventsData extends SQLiteOpenHelper {
    public EventsData(Context ctx){
        super(ctx, "events.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE + " TEXT, "
                + WEIGHT + " INTEGER, "
                + BMI + " TEXT, "
                + STANDARD + " TEXT );"  );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
}
