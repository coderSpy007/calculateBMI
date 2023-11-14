package com.example.assignment1;

import static com.example.assignment1.Constants.BMI;
import static com.example.assignment1.Constants.DATE;
import static com.example.assignment1.Constants.STANDARD;
import static com.example.assignment1.Constants.TABLE_NAME;
import static com.example.assignment1.Constants.WEIGHT;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
private EventsData ev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ev = new EventsData(MainActivity2.this);
       try {
            Cursor cur = getEvents();
            showEvents(cur);
        }
        finally {
            ev.close();
        }
       final Button bt = findViewById(R.id.button2);
       bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ev = new EventsData(MainActivity2.this);
                try{
                    resetAutoInc();
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    ev.close();
                }
            }
        });
    }
    private Cursor getEvents() {
        String[] FROM = {DATE, WEIGHT, BMI, STANDARD};
        String ORDER_BY = DATE + " DESC";
        SQLiteDatabase db = ev.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        return cursor;
    }//end getEvents

    private void showEvents(Cursor cursor) {
        final ListView listView = (ListView)findViewById(R.id.lv);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        while(cursor.moveToNext()) {
            map = new HashMap<String, String>();
            map.put("Date", cursor.getString(0));
            map.put("Weight", String.valueOf(cursor.getLong(1)));
            map.put("bmi", cursor.getString(2));
            map.put("Standard", cursor.getString(3));
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter( MainActivity2.this, MyArrList, R.layout.column,
                new String[] {"Date", "Weight", "bmi", "Standard"},
                new int[] {R.id.date, R.id.weight, R.id.bmi, R.id.std} );
        listView.setAdapter(sAdap);
    }

    private void resetAutoInc() {
        SQLiteDatabase db = ev.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
    }//end resetAutoInc

}