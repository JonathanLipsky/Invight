package com.example.lokid.projectfalcon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class create_event_fragment extends Fragment {


    DatabaseHandler database;

    public create_event_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_create_event_fragment, container, false);

        database = new DatabaseHandler();



        Button button = (Button) view.findViewById(R.id.create_event);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event new_event = new Event();
                EditText txtTitle = (EditText) v.findViewById(R.id.event_name);
                String title = txtTitle.getText().toString();
                new_event.setTitle(title);
                new_event.setSnippet(((TextView)v.findViewById(R.id.event_desc)).getText().toString());
                new_event.setHost("Dillon");

                Spinner spinner = (Spinner)v.findViewById(R.id.event_types);
                String eventTpye = spinner.getSelectedItem().toString();
                //new_event.setEventType(eventTpye);



                String sTime =(((TextView)v.findViewById(R.id.event_start_time)).getText().toString());
                String sDate = (((TextView)v.findViewById(R.id.event_start_date)).getText().toString());
                String eTime =(((TextView)v.findViewById(R.id.event_end_time)).getText().toString());
                String eDate = (((TextView)v.findViewById(R.id.event_end_date)).getText().toString());


            }
        });
        //container.findViewById(R.id.)

        //database.addEvent();


        return view;


    }


}
