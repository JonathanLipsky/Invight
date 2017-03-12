package com.example.lokid.projectfalcon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Button buttonLogout;
    FirebaseAuth firebaseAuth;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Settings");

        firebaseAuth = FirebaseAuth.getInstance();

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonLogout = (Button)view.findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();
            }
        });

        return view;
    }

    private void userLogout(){

        if(firebaseAuth.getInstance().getCurrentUser()!=null){
            firebaseAuth.signOut();
            Toast.makeText(getActivity(), "Signed out Successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "You are not Signed in", Toast.LENGTH_SHORT).show();
        }

    }

}
