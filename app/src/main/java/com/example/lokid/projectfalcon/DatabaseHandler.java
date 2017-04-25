package com.example.lokid.projectfalcon;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.geofire.*;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class grabs and sets data in the database
 * TO DO'S
 *      - change event class to handle a timestamp, then add Event remove
 *      -add authentication
 *      -add a way to store photos that can be easily accessed in the database
 *       and stored as a reference
 *      -way to get 24hour, 1 week, and all events on a rolling time
 *      -private events
 */

public class DatabaseHandler {

    private HashMap<String,GeoLocation> keys;
    private int numberOfKeys;
    private int keysAddedCurrently;
    private LinkedList<LinkedList<Event>> events;
    private DatabaseReference root;
    private DataListener lis = new DataListener();
    private GeoFire fire;
    private GeoListener listener;
    private RetrieveEventListener eventListener;
    private int searchTime;

    public DatabaseHandler()
    {
        keys = new HashMap<>();
        events = new LinkedList<>();
        searchTime = 0;
    }

    public void addEvent(Event event) {
        root = FirebaseDatabase.getInstance().getReference().child("Events");
        String id = root.push().getKey();
        root.child(id).setValue(event);
        //^sets up the itemStructure
        fire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Event_Locations"));
        fire.setLocation(id,new GeoLocation(event.getLat(),event.getLong()));
        //sets up parallel geofire object that represents the location of other object
    }

    public void retrieveEvents(Location center, Location corner)
    {
        numberOfKeys = 0;
        keysAddedCurrently = 0;
        root = FirebaseDatabase.getInstance().getReference().child("Events");
        fire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Event_Locations"));
        GeoQuery query = fire.queryAtLocation(new GeoLocation(center.getLatitude(),center.getLongitude()),center.distanceTo(corner)/1000);
        listener = new GeoListener();
        query.addGeoQueryEventListener(listener);
    }

    public void setSearchTime(int time)
    {
        if(time < 2 && time >= 0)
            searchTime = time;
    }

    public void addUser(Profile profile)
    {
        root = FirebaseDatabase.getInstance().getReference().child("Profiles").child(profile.getUsername());
        root.setValue(profile);
    }

    public void getUser(String username, final String password)
    {

        root = FirebaseDatabase.getInstance().getReference().child("Profiles");
        root.child(username);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if(profile.getPassword() == password)
                        eventListener.getProfile(profile);
                    else
                        eventListener.getProfile(null);
                }
                else
                    eventListener.getProfile(null);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addKeys(int amount)
    {
        Iterator<String> keys = this.keys.keySet().iterator();
        while(keys.hasNext()) {
            root = FirebaseDatabase.getInstance().getReference().child("Events");
            root = root.child(keys.next());
            root.addListenerForSingleValueEvent(lis);
        }
        numberOfKeys = amount;
    }

    private void addValue(Event e){
        boolean added = false;
        LatLng pos;
        for(int i = 0; i < events.size(); i++)
        {
            pos = events.get(i).getFirst().getPosition();
            if(pos.latitude == e.getLat() && pos.longitude == e.getLong())
            {
                events.get(i).add(e);
                added = true;
                break;
            }
        }
        if(!added)
        {
            LinkedList<Event> list = new LinkedList<>();
            list.add(e);
            events.add(list);
        }
        keysAddedCurrently++;
        if(numberOfKeys == keysAddedCurrently)
           returnSortedEvents();
    }

    private void returnSortedEvents()
    {
        Event[] eventsSorted = new Event[events.size()];
        LinkedList<Event> temp;
        Event minValue;
        for(int i = 0; i < events.size(); i++)
        {
            temp =  events.get(i);
            minValue = temp.get(0);
            for(int j = 1; j < temp.size(); j++)
            {
                if(temp.get(j).getPopularity() < minValue.getPopularity())
                {
                    minValue = temp.get(j);
                }
            }
            eventsSorted[i] = minValue;

        }
        eventListener.getEvents(eventsSorted);
    }

    private class GeoListener implements GeoQueryEventListener
    {
        private int numEvents = 0;

        @Override
        public void onKeyEntered(String key, GeoLocation location)
        {
            keys.put(key,location);
            numEvents++;
        }

        @Override
        public void onKeyExited(String key) {

        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {
            addKeys(numEvents);
        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }

    }

    private class DataListener implements ValueEventListener{
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Event e = dataSnapshot.getValue(Event.class);
            GeoLocation temp = keys.get(dataSnapshot.getKey());
            e.setLatlng(new LatLng(temp.latitude,temp.longitude));
            e.setKey(dataSnapshot.getKey());
            addValue(e);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

    public void addEventLister(RetrieveEventListener listener)
    {
        eventListener = listener;
    }

    public interface RetrieveEventListener {
        void getEvents(Event[] events);
        void getProfile(Profile profile);
    }


}