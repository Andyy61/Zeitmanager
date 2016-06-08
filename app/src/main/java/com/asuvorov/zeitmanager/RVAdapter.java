package com.asuvorov.zeitmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ASuvorov on 02.06.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DayViewHolder> {

    List<DayDataProvider> days;
    Context context;

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dayrowlayout, parent, false);
        DayViewHolder pvh = new DayViewHolder(v, context);
        return pvh;
    }

    RVAdapter(List<DayDataProvider> days, Context context) {
        this.days = days;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        holder.dayName.setText(days.get(position).dayName);
        holder.dayDate.setText(days.get(position).date);
        holder.gehenTextView.setText(days.get(position).gehen);
        holder.kommenTextView.setText(days.get(position).kommen);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy",Locale.GERMANY);
        String date = df.format(Calendar.getInstance().getTime());
        try {
            Date now = df.parse(date);
            Date current = df.parse(holder.dayDate.getText().toString());

            if(!now.equals(current))
            {
                holder.pauseButton.setVisibility(View.INVISIBLE);
                holder.gehenButton.setVisibility(View.INVISIBLE);
                holder.kommenButton.setVisibility(View.INVISIBLE);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }


    public class DayViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView dayName, dayDate, pauseTextView, gehenTextView, kommenTextView;
        Button gehenButton, kommenButton, pauseButton;
        DbHelper dbHelper;
        boolean pauseAngefangen = false;
        String pausenBeginn = "";

        DayViewHolder(View itemView, final Context context) {
            super(itemView);
            dbHelper = new DbHelper(context);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailActivity3.class);
                    Bundle b = new Bundle();
                    b.putString("date", dayDate.getText().toString());
                    intent.putExtras(b); //Put your id to your next Intent
                    context.startActivity(intent);
                }
            });

            dayName = (TextView) itemView.findViewById(R.id.dayNameTextView);
            dayDate = (TextView) itemView.findViewById(R.id.dateTextView);
            gehenTextView = (TextView) itemView.findViewById(R.id.gehenTextView);
            pauseTextView = (TextView) itemView.findViewById(R.id.pauseTextView);
            kommenTextView = (TextView) itemView.findViewById(R.id.kommenTextView);
            gehenButton = (Button) itemView.findViewById(R.id.gehenButton);
            kommenButton = (Button) itemView.findViewById(R.id.kommenButton);
            pauseButton = (Button) itemView.findViewById(R.id.pauseButton);


            gehenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    dbHelper.updateGehen(dayDate.getText().toString(), date.toString());
                    gehenTextView.setText(date);
                }
            });
            kommenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    dbHelper.updateKommen(dayDate.getText().toString(), date.toString());
                    kommenTextView.setText(date);
                }
            });
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (pauseAngefangen) {
                        DateFormat df = new SimpleDateFormat("HH:mm", Locale.GERMANY);
                        pausenBeginn = df.format(Calendar.getInstance().getTime());
                        pauseAngefangen = true;
                        pauseButton.setText("Aufhören");
                    } else {
                        DateFormat df = new SimpleDateFormat("HH:mm", Locale.GERMANY);
                        String pausenEnde = df.format(Calendar.getInstance().getTime());

                        //HH converts hour in 24 hours format (0-23), day calculation
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.GERMANY);

                        Date d1 = null;
                        Date d2 = null;

                        try {
                            d1 = format.parse(pausenBeginn);
                            d2 = format.parse(pausenEnde);

                            long difference = d2.getTime() - d1.getTime();
                            Date d3 = new Date(difference);

                            Date vorherigePause = format.parse(pauseTextView.getText().toString());
                            vorherigePause.setHours(d3.getHours());
                            vorherigePause.setMinutes(d3.getMinutes());
                            pauseAngefangen = false;
                            pauseTextView.setText(df.format(vorherigePause));
                            pausenBeginn = "";
                            dbHelper.updatePause(dayDate.getText().toString(), pauseTextView.getText().toString());
                            pauseButton.setText("Beginnen");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}