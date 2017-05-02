package com.example.lokid.projectfalcon;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class event_list extends Fragment {

    ArrayList<Event> events;
    ListView listView;
    final View view = null;

    public event_list() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        LatLng pos = new LatLng(bundle.getDouble("lat"),bundle.getDouble("lng"));
        events = MainActivity.getDatabaseReference().getEvents(pos);
        String eventsAddress = bundle.getString("address");
        final View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        listView = (ListView)view.findViewById(R.id.lvEvents);
        //events = new ArrayList<Event>();
        try{

            listView.setAdapter(new EventAdapter(getActivity(), events));
        }catch(Exception e){
            Toast.makeText(getActivity(), "Error retrieving events", Toast.LENGTH_LONG).show();
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void initItemClick(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = events.get(position);
                /*Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                intent.putExtra("contactid",selectedRestaurant.getRestaurantId());
                startActivity(intent);*/
                Bundle args = new Bundle();
                args.putString("eventID", selectedEvent.getKey());
                /*RestaurantDialog rd = new RestaurantDialog();
                rd.setArguments(args);
                rd.show(getFragmentManager(), "ratePicker");*/

            }
        });
    }
}
