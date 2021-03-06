package com.asuvorov.zeitmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by ASuvorov on 06.06.2016.
 *
 */
public class TimePickerFragmentEnde extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        EditText endeEditText = (EditText) getActivity().findViewById(R.id.endeEditText);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        endeEditText.setText(decimalFormat.format(hourOfDay) +":" + decimalFormat.format(minute));
    }
}