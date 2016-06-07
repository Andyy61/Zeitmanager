package com.asuvorov.zeitmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASuvorov on 07.06.2016.
 */
public class TimeListViewAdapter extends ArrayAdapter {
    ArrayList list = new ArrayList() ;
    Context context;
    public TimeListViewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.kommengehenlayout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.kommenTime = (EditText) row.findViewById(R.id.kommenTimeEditText);
            layoutHandler.gehenTime = (EditText) row.findViewById(R.id.gehenTimeEditText);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }
        TimeListViewDataProvider dataProvider = (TimeListViewDataProvider) this.getItem(position);
        layoutHandler.kommenTime.setText(dataProvider.getKommenZeit());
        layoutHandler.gehenTime.setText(dataProvider.getGehenZeit());

        return row;
    }

    static class LayoutHandler
    {
        EditText kommenTime;
        EditText gehenTime;
    }
}
