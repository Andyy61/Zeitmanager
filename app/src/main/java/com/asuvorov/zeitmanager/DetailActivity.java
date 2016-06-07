package com.asuvorov.zeitmanager;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {


    EditText endeEditText, pauseEditText;
    TextView bruttoStundenTextView, nettoStundenTextView,ueberBetragTextView,weekDayTextView;
    EditText beginnEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        String date = "";
        if(b != null)
            date = b.getString("date");
        Toast.makeText(this,date,Toast.LENGTH_LONG).show();
        beginnEditText = (EditText) findViewById(R.id.beginnEditText);
        endeEditText = (EditText) findViewById(R.id.endeEditText);
        pauseEditText = (EditText) findViewById(R.id.pauseEditText);
        bruttoStundenTextView = (TextView) findViewById(R.id.bruttoStundenTextView);
        nettoStundenTextView = (TextView) findViewById(R.id.nettoStundenTextView);
        ueberBetragTextView = (TextView) findViewById(R.id.ueberbetragTextView);
        weekDayTextView = (TextView) findViewById(R.id.weekDayTextView);
        DbHelper db = new DbHelper(this);

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
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        try {
            Date kommenTime = new Date(df.parse(beginnEditText.getText().toString()).getTime());
            Date gehenTime = new Date(df.parse(endeEditText.getText().toString()).getTime());
            Date pausenTime = new Date(df.parse(pauseEditText.getText().toString()).getTime());

            long differenceBrutto = gehenTime.getTime() - kommenTime.getTime();
            Date brutto = new Date(differenceBrutto);
            Toast.makeText(this,brutto.toString(),Toast.LENGTH_LONG).show();
            bruttoStundenTextView.setText(brutto.toString());
            long differenceNetto = brutto.getTime() - pausenTime.getTime();
            Toast.makeText(this,brutto.toString() + " = " + kommenTime.toString(),Toast.LENGTH_LONG).show();
            Date netto = new Date(df.format(differenceNetto));
            Toast.makeText(this,netto.toString(),Toast.LENGTH_LONG).show();
            nettoStundenTextView.setText(netto.toString());

        } catch (ParseException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
