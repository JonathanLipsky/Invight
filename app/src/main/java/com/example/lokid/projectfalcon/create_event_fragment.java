package com.example.lokid.projectfalcon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

        //database.addEvent();


        return inflater.inflate(R.layout.fragment_create_event_fragment, container, false);


    }


}
