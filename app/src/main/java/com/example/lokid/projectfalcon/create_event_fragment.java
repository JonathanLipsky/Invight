package com.example.lokid.projectfalcon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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



        database = new DatabaseHandler();



        Button button = (Button) container.findViewById(R.id.create_event);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event new_event = new Event();

                new_event.setTitle(((TextView)v.findViewById(R.id.event_name)).getText().toString());
                new_event.setSnippet(((TextView)v.findViewById(R.id.event_desc)).getText().toString());
                new_event.setHost("Dillon");


                //Date sTime =(((TextView)v.findViewById(R.id.event_start_time)).getText().toString());
                //Date sDate = (((TextView)v.findViewById(R.id.event_start_date)).getText().toString());


            }
        });
        //container.findViewById(R.id.)

        //database.addEvent();


        return inflater.inflate(R.layout.fragment_create_event_fragment, container, false);


    }


}
