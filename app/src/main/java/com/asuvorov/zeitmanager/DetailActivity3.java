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

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DetailActivity3 extends AppCompatActivity implements TextWatcher {


    ListView listView;
    TimeListViewAdapter adapter;
    TextView nettoStundenTextView, bruttoStundenTextView, ueberBetragTextView;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);

        Bundle b = getIntent().getExtras();
        String date = "";
        if(b != null)
            date = b.getString("date");
        listView = (ListView)findViewById(R.id.listView);
        db = new DbHelper(this);
        Cursor cursor = db.getInformation(date);
        if(cursor.moveToFirst()) {
            adapter = new TimeListViewAdapter(this, R.layout.kommengehenlayout);
            listView.setAdapter(adapter);
           do {
               adapter.add(new TimeListViewDataProvider(cursor.getString(3),cursor.getString(5)));
           }while (cursor.moveToNext());
       // berechneAlles();

        }
    }

    private void berechneAlles() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm", Locale.GERMANY);
        try {

            if(adapter.getCount() > 1)
            {
                Date tmpBrutto = null;
                Date tmpPause = new Date(0);
                Date letztesGehen = null;
                for (int i = 0; i < adapter.getCount();i++)
                {
                    TimeListViewDataProvider tmpa =(TimeListViewDataProvider) adapter.getItem(i);

                    letztesGehen = new Date(df.parse(tmpa.gehenZeit).getTime());

                    if(!tmpa.gehenZeit.equals("--:--"))
                    {
                        if(tmpBrutto == null) {
                            tmpBrutto = df.parse(getDifferenceDate(df.parse(tmpa.kommenZeit), df.parse(tmpa.gehenZeit)));
                        }
                        else
                        {
                            tmpBrutto = new Date(tmpBrutto.getTime() + df.parse(getDifferenceDate(df.parse(tmpa.kommenZeit), df.parse(tmpa.gehenZeit))).getTime());
                        }
                    }

                    tmpPause = new Date(tmpPause.getTime() + df.parse(getDifferenceDate(letztesGehen, df.parse(tmpa.kommenZeit))).getTime());



                }
                Date netto = new Date(df.parse(getDifferenceDate(tmpPause,tmpBrutto)).getTime());
                Date nettoTime = new Date(df.parse(nettoStundenTextView.getText().toString()).getTime());
                Date amTag = new Date(df.parse("08:00").getTime()); //Aus den Einstellungen lesen
                nettoStundenTextView.setText(df.format(netto));
                bruttoStundenTextView.setText(df.format(tmpBrutto));
                ueberBetragTextView.setText(getDifferenceDate(amTag,nettoTime));
            }
          /* Date kommenTime = new Date(df.parse(beginnEditText.getText().toString()).getTime());
            Date gehenTime = new Date(df.parse(endeEditText.getText().toString()).getTime());
            Date pausenTime = new Date(df.parse(pauseEditText.getText().toString()).getTime());
            bruttoStundenTextView.setText(getDifferenceDate(kommenTime,gehenTime));

            Date bruttoTime = new Date(df.parse(bruttoStundenTextView.getText().toString().toString()).getTime());
            nettoStundenTextView.setText(getDifferenceDate(pausenTime,bruttoTime));


            Date nettoTime = new Date(df.parse(nettoStundenTextView.getText().toString()).getTime());
            Date amTag = new Date(df.parse("08:00").getTime()); //Aus den Einstellungen lesen

            ueberBetragTextView.setText(getDifferenceDate(amTag,nettoTime));
*/

        } catch (ParseException e) {
            e.printStackTrace();
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
        //db.updateDay(dateTextView.getText().toString(),beginnEditText.getText().toString(),pauseEditText.getText().toString(), endeEditText.getText().toString());

    }
}
