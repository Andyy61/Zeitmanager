package com.asuvorov.zeitmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<DayDataProvider> days;
    private DbHelper dbHelper;
    private void initializeData() {
        days = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        String date = df.format(Calendar.getInstance().getTime());

        Cursor cursor = dbHelper.getWeek(date);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String tmpKommen = cursor.getString(3);
                    Cursor cursor1 = dbHelper.getInformation(cursor.getString(0));
                    if(cursor1.moveToLast())
                    {
                        days.add(new DayDataProvider(cursor1.getString(2), cursor1.getString(0), tmpKommen, cursor1.getString(5), cursor1.getString(4)));
                    }
                    else
                    {
                        cursor1.moveToFirst();
                        days.add(new DayDataProvider(cursor1.getString(2), cursor1.getString(0), cursor1.getString(0), cursor1.getString(5), cursor1.getString(4)));
                    }

                } while (cursor.moveToNext());

            } else {
                dbHelper.setDaysOfWeek(new Date());
                cursor = dbHelper.getWeek(date);
                if(cursor.moveToFirst()) {
                    do {
                        days.add(new DayDataProvider(cursor.getString(2), cursor.getString(0), cursor.getString(3), cursor.getString(5), cursor.getString(4)));
                    } while (cursor.moveToNext());
                }
            }
        } else {
            dbHelper.setDaysOfWeek(new Date());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        if (rv != null) {
            rv.setHasFixedSize(true);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this.getApplicationContext());
        if (rv != null) {
            rv.setLayoutManager(llm);
        }
        initializeData();
        RVAdapter adapter = new RVAdapter(days, this);
        if (rv != null) {
            rv.setAdapter(adapter);
        }
    }
}

