package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.assignment1.Constants.TABLE_NAME;
import static com.example.assignment1.Constants.BMI;
import static com.example.assignment1.Constants.DATE;
import static com.example.assignment1.Constants.WEIGHT;
import static com.example.assignment1.Constants.STANDARD;




public class MainActivity extends AppCompatActivity {
    private EventsData ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edit_text = (EditText)findViewById(R.id.edT);
        EditText edit_text1 = (EditText)findViewById(R.id.edT1);
        EditText edit_text2 = (EditText)findViewById(R.id.edT2);
        EditText edit_text3 = (EditText)findViewById(R.id.edT3);


        edit_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        edit_text1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});


        final Button bt = (Button) findViewById(R.id.button2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double w = Float.parseFloat(edit_text.getText().toString());
                double h = Float.parseFloat(edit_text1.getText().toString());

                if(edit_text.getText().toString().isEmpty()&&edit_text1.getText().toString().isEmpty())
                {
                    w=0;
                    h=0;
                    edit_text.setText("0");
                    edit_text1.setText("0");
                }


                double bmi = w/(h/100*h/100);
                DecimalFormat formatter = new DecimalFormat("#,###.##");
                String bmi2 = formatter.format(bmi);
                edit_text2.setText(bmi2+"");


                if(bmi < 16)
                {
                    edit_text3.setText(R.string.severe);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.hardRed));
                }
                else if(bmi >= 16 && bmi<=17)
                {
                    edit_text3.setText(R.string.moderate);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.lightRed));
                }
                else if(bmi >= 17 && bmi<=18.5)
                {
                    edit_text3.setText(R.string.mild);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.lightRed));
                }
                else if(bmi >= 18.5 && bmi<=25)
                {
                    edit_text3.setText(R.string.nor);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else if(bmi >= 25 && bmi<=30)
                {
                    edit_text3.setText(R.string.over);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.lightRed));
                }
                else if(bmi >= 30 && bmi<=35)
                {
                    edit_text3.setText(R.string.obI);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.lightRed));
                }
                else if(bmi >= 35 && bmi<=40)
                {
                    edit_text3.setText(R.string.obII);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.lightRed));
                }
                else if(bmi > 40)
                {
                    edit_text3.setText(R.string.obIII);
                    edit_text3.setBackgroundColor(getResources().getColor(R.color.hardRed));
                }

                ev = new EventsData(MainActivity.this);
                addEvent();

                ev.close();
            }


        });
        final ImageButton histo = (ImageButton) findViewById(R.id.htr);
        histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(it);
            }
        });

    }
    private void addEvent() {
        EditText et = (EditText) findViewById(R.id.edT);
        EditText et2 = (EditText) findViewById(R.id.edT2);
        EditText et3 = (EditText) findViewById(R.id.edT3);
        Integer weight = Integer.parseInt(et.getText().toString());
        String bmi = String.format("%1$s",et2.getText());
        String std = String.format("%1$s",et3.getText());
        SQLiteDatabase db = ev.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String current = sdf.format(new Date());
        values.put(DATE,current);
        values.put(WEIGHT,weight);
        values.put(BMI,bmi);
        values.put(STANDARD,std);
        db.insert(TABLE_NAME,null,values);
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
            map.put("Date", String.valueOf(cursor.getLong(0)));
            map.put("Weight", String.valueOf(cursor.getLong(1)));
            map.put("bmi", cursor.getString(2));
            map.put("Standard", cursor.getString(3));
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter( MainActivity.this, MyArrList, R.layout.column,
                new String[] {"Date", "Weight", "bmi", "Standard"},
                new int[] {R.id.date, R.id.weight, R.id.bmi, R.id.std} );
        listView.setAdapter(sAdap);
    }//end showEvents



    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        public DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +  "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }
}
    

    
