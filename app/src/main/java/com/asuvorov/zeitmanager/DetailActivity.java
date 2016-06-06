package com.asuvorov.zeitmanager;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextWatcher {


    EditText endeEditText, pauseEditText;
    TextView bruttoStundenTextView, nettoStundenTextView,ueberBetragTextView,weekDayTextView, dateTextView;
    EditText beginnEditText;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        String date = "";
        if(b != null)
            date = b.getString("date");
        Toast.makeText(this,date,Toast.LENGTH_LONG).show();
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(date);
        beginnEditText = (EditText) findViewById(R.id.beginnEditText);
        beginnEditText.addTextChangedListener(this);
        endeEditText = (EditText) findViewById(R.id.endeEditText);
        endeEditText.addTextChangedListener(this);
        pauseEditText = (EditText) findViewById(R.id.pauseEditText);
        pauseEditText.addTextChangedListener(this);
        bruttoStundenTextView = (TextView) findViewById(R.id.bruttoStundenTextView);
        nettoStundenTextView = (TextView) findViewById(R.id.nettoStundenTextView);
        ueberBetragTextView = (TextView) findViewById(R.id.ueberbetragTextView);
        weekDayTextView = (TextView) findViewById(R.id.weekDayTextView);
        db = new DbHelper(this);

        Cursor cursor = db.getInformation(date);

        if(cursor.moveToFirst()) {
            do {
                Toast.makeText(this,cursor.getString(2),Toast.LENGTH_LONG).show();
                beginnEditText.setText(cursor.getString(3));
                endeEditText.setText(cursor.getString(5));
                pauseEditText.setText(cursor.getString(4));
                weekDayTextView.setText(cursor.getString(2));
                berechneAlles();
            } while (cursor.moveToNext());

        }


        beginnEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragmentBeginn();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        endeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragmentEnde();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
    }

    private void berechneAlles() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm", Locale.GERMANY);
        try {
            Date kommenTime = new Date(df.parse(beginnEditText.getText().toString()).getTime());
            Date gehenTime = new Date(df.parse(endeEditText.getText().toString()).getTime());
            Date pausenTime = new Date(df.parse(pauseEditText.getText().toString()).getTime());

            long differenceBrutto = gehenTime.getTime() - kommenTime.getTime();

        
            Date brutto = df.parse(getDifferenceDate(differenceBrutto));

            bruttoStundenTextView.setText(df.format(brutto));
            long differenceNetto = brutto.getTime() - pausenTime.getTime();

            Date netto = df.parse(getDifferenceDate(differenceNetto));
            nettoStundenTextView.setText(df.format(netto));

            Date amTag = new Date(df.parse("08:00").getTime());

            int hours = netto.getHours() - amTag.getHours();
            int minutes = netto.getMinutes() - amTag.getMinutes();



            Date ueber = df.parse(df.format(String.valueOf(hours )+ ":" + String.valueOf(minutes)));
            ueberBetragTextView.setText(df.format(ueber));


        } catch (ParseException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    
    private String getDifferenceDate(long diff)
    {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;
        return elapsedHours + ":" + elapsedMinutes;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        berechneAlles();
        db.updateDay(dateTextView.getText().toString(),beginnEditText.getText().toString(),pauseEditText.getText().toString(), endeEditText.getText().toString());

    }
}
