package com.example.lokid.projectfalcon;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by lokid on 4/26/2017.
 */

public class TimePickerDialog extends DialogFragment {


    public interface SaveTimeListener{
        void didFinishTimePickerDialog(int hour, int min);
    }

    public TimePickerDialog(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.select_time, container);
        getDialog().setTitle("Select Time");


        final TimePicker tp = (TimePicker) view.findViewById(R.id.timePicker);
        Button saveButton = (Button)view.findViewById(R.id.buttonSelect);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedTime = Calendar.getInstance();
                int hour = tp.getHour();
                int min = tp.getMinute();
                saveItem(hour, min);
            }
        });
        Button cancelButton = (Button)view.findViewById(R.id.buttonCancle);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void saveItem(int hour, int min){
        TimePickerDialog.SaveTimeListener fragment = (TimePickerDialog.SaveTimeListener)getTargetFragment();
        fragment.didFinishTimePickerDialog(hour, min);
        getDialog().dismiss();
    }


}
