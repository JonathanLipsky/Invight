package com.example.lokid.projectfalcon;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class create_event_fragment extends Fragment implements DatePickerDialog.SaveDateListener, TimePickerDialog.SaveTimeListener {

    public static final int CREATE_EVENT_FRAGMENT = 1;
    DatabaseHandler database;
    private Fragment fragment = this;
    public create_event_fragment() {
        // Required empty public constructor
    }

    private int whichTime;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_create_event_fragment, container, false);

        database = new DatabaseHandler();

        Button buttonDatePick = (Button)view.findViewById(R.id.btnStartDate);
        buttonDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.setTargetFragment(fragment, 1);

                datePickerDialog.show(getFragmentManager(), "DatePick");
            }
        });

        Button buttonStartTimePick = (Button)view.findViewById(R.id.btnStartTime);
        buttonStartTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichTime = 1;
                TimePickerDialog timePickerDialog = new TimePickerDialog();
                timePickerDialog.setTargetFragment(fragment, 2);
                timePickerDialog.show(getFragmentManager(), "Time Pick");
            }
        });

        Button buttonEndTimePick = (Button)view.findViewById(R.id.btnEndTime);
        buttonEndTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichTime = 2;
                TimePickerDialog timePickerDialog = new TimePickerDialog();
                timePickerDialog.setTargetFragment(fragment, 3);
                timePickerDialog.show(getFragmentManager(), "Time Pick");
            }
        });

        Button buttonCreate = (Button) view.findViewById(R.id.btnCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event new_event = new Event();
                EditText etTitle = (EditText)view.findViewById(R.id.editEventName);
                String title = etTitle.getText().toString();
                EditText etDescription = (EditText)view.findViewById(R.id.editEventDescription);
                String description = etDescription.getText().toString();
                Spinner sEventTypes = (Spinner)view.findViewById(R.id.event_types);
                String eventType = sEventTypes.getSelectedItem().toString();





            }
        });
        //container.findViewById(R.id.)

        //database.addEvent();


        return view;
    }

    public void didFinishDatePickerDialog(Calendar selectedTime) {
        selectedDate = selectedTime;
        TextView startDate = (TextView)view.findViewById(R.id.txtShowStartDate);
        startDate.setText(DateFormat.format("MM/dd/yyyy",selectedTime.getTimeInMillis()).toString());
    }
    private int startHour;
    private int endHour;
    private int startMin;
    private int endMin;
    private Calendar selectedDate;
    @Override
    public void didFinishTimePickerDialog(int hour, int min) {
        TextView time = null;
        if(whichTime==1){
            time = (TextView)view.findViewById(R.id.txtShowStartTime);
            startHour = hour;
            startMin = min;
        }else if(whichTime==2){
            time = (TextView)view.findViewById(R.id.txtShowEndTime);
            endHour = hour;
            endMin = min;
        }

        String format = "";
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }


        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }
}
