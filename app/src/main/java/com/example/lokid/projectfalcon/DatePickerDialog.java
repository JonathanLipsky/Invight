package com.example.lokid.projectfalcon;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by lokid on 2/27/2017.
 */

public class DatePickerDialog extends DialogFragment{

    public interface SaveDateListener{
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.select_date, container);
         getDialog().setTitle("Select Date");

        final DatePicker dp = (DatePicker)view.findViewById(R.id.birthdayPicker);

        Button saveButton = (Button)view.findViewById(R.id.buttonSelect);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                saveItem(selectedTime);
            }
        });
        Button cancelButton = (Button)view.findViewById(R.id.buttonCancle);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void saveItem(Calendar selectedTime){
        SaveDateListener fragment = (SaveDateListener)getTargetFragment();
        fragment.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }
}
