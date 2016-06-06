package com.asuvorov.zeitmanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        String date = df.format(Calendar.getInstance().getTime());

        Cursor cursor = dbHelper.getWeek(date);

            if (cursor.moveToFirst()) {
                do {
                    days.add(new DayDataProvider(cursor.getString(2), cursor.getString(0), cursor.getString(3), cursor.getString(5), cursor.getString(4)));
                } while (cursor.moveToNext());

            } else {
                dbHelper.setDaysOfWeek(new Date());
                do {
                    days.add(new DayDataProvider(cursor.getString(2), cursor.getString(0), cursor.getString(3), cursor.getString(5), cursor.getString(4)));
                } while (cursor.moveToNext());
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

