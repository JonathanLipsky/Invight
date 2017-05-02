package com.example.lokid.projectfalcon;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class screen1 extends Fragment {


    public screen1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("");
        return inflater.inflate(R.layout.fragment_screen1, container, false);
    }

}
