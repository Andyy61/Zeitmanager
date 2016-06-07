package com.asuvorov.zeitmanager;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

            bruttoStundenTextView.setText(getDifferenceDate(kommenTime,gehenTime));

            Date bruttoTime = new Date(df.parse(bruttoStundenTextView.getText().toString().toString()).getTime());
            nettoStundenTextView.setText(getDifferenceDate(pausenTime,bruttoTime));


            Date nettoTime = new Date(df.parse(nettoStundenTextView.getText().toString()).getTime());
            Date amTag = new Date(df.parse("08:00").getTime()); //Aus den Einstellungen lesen

            ueberBetragTextView.setText(getDifferenceDate(amTag,nettoTime));


        } catch (ParseException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private String getDifferenceDate(Date date1, Date date2)
    {
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(date1);

        Calendar endDate = new GregorianCalendar();
        endDate.setTime(date2);


        long totalMillis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        int minutes =  ((int)(totalMillis / 1000) / 60) % 60;
        int hours = (int)(totalMillis / 1000) / 3600;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(hours) +":" + decimalFormat.format(minutes);
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
