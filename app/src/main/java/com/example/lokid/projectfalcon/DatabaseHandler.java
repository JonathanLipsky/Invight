package com.example.lokid.projectfalcon;

import android.location.Location;
import android.provider.Settings;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.geofire.*;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private HashMap<String,GeoLocation> tempKeys;
    private int numberOfKeys;
    private int keysAddedCurrently;
    private ArrayList<Event> events;
    private ArrayList<Event> tempEvents;
    private DatabaseReference root;
    private DataListener lis = new DataListener();
    private GeoFire fire;
    private GeoListener listener;
    private RetrieveEventListener eventListener;
    private int searchTime;
    private boolean searching;
    private final long dayInMilli = 86400000;
    private final long weekInMilli = 604800000;
    private Profile mainUser;
    private ArrayList<Event> followedEvents = new ArrayList<>(10);

    public DatabaseHandler()
    {
        keys = new HashMap<>();
        events = new ArrayList<>(50);
        tempEvents = new ArrayList<>();
        tempKeys = new HashMap<>();
        searchTime = 0;
        searching = false;
    }

    public static void addEvent(Event event) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Events");
        String id = root.push().getKey();
        root.child(id).setValue(event);
        //^sets up the itemStructure
        GeoFire fire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Event_Locations"));
        fire.setLocation(id,new GeoLocation(event.getLat(),event.getLong()));
        //sets up parallel geofire object that represents the location of other object
    }

    public void retrieveEvents(Location center, Location corner)
    {
        searching = true;
        numberOfKeys = 0;
        keysAddedCurrently = 0;
        fire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Event_Locations"));
        GeoQuery query = fire.queryAtLocation(new GeoLocation(center.getLatitude(),center.getLongitude()),center.distanceTo(corner)/1000);
        listener = new GeoListener();
        query.addGeoQueryEventListener(listener);
    }

    public void setSearchTime(int time)
    {
        if(time < 3 && time >= 0)
            searchTime = time;
    }

    public Event[] mostPopularEventsInArea(int howMany)
    {
        Event[] popularEvents = new Event[howMany];
        for (int i = 0; i < howMany; i++)
        {
            int largest = i;
            for(int j = i+1; i < events.size();i++)
            {
                if(events.get(largest).getPopularity() < events.get(j).getPopularity())
                    largest = j;
            }
            popularEvents[i] = events.get(largest);
            events.remove(largest);
            events.add(i,popularEvents[i]);
        }

        return popularEvents;
    }

    public ArrayList<Event> getFollowedEvents()
    {
        return followedEvents;
    }


    public void eventLiked(Event e,boolean liked)
    {
        if(e.getKey() == null)
            return;
        root = FirebaseDatabase.getInstance().getReference().child("Events").child(e.getKey()).child("popularity");
        if(liked)
            e.setPopularity(e.getPopularity()+1);
        else
            e.setPopularity(e.getPopularity()-1);
        root.setValue(e.getPopularity());
        if(mainUser == null)
            return;
        root = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mainUser.getUsername());

        if(liked) {
            if(mainUser.getLocationsPreference().containsKey(e.getEventType())) {
                mainUser.getLocationsPreference().put(e.getEventType(), mainUser.getLocationsPreference().get(e.getEventType()) + 1);
                root.child("favEvents").child(e.getKey()).setValue(e.getPopularity());
            }
            else
                root.child("favEvents").child(e.getKey()).setValue(1);
        }
        else if(!liked){
            if(mainUser.getLocationsPreference().containsKey(e.getEventType())) {
                mainUser.getLocationsPreference().put(e.getEventType(), mainUser.getLocationsPreference().get(e.getEventType()) - 1);
                root.child("favEvents").child(e.getKey()).removeValue();
            }
        }
        if(mainUser.getLocationsPreference().containsKey(e.getEventType()))
            root.child("locationsPreference").child(e.getEventType()).setValue(mainUser.getLocationsPreference().get(e.getEventType()));
        else
            root.child("locationsPreference").child(e.getEventType()).setValue(1);

        getUser(mainUser.getUsername(),mainUser.getPassword());
    }

    public ArrayList<Event> getEvents(LatLng position)
    {
        //add in filter*
        Long time = (System.currentTimeMillis());
        ArrayList<Event> temp = new ArrayList<>();
        for(int i = 0; i < events.size(); i++)
        {
            LatLng pos = events.get(i).getPosition();
            if(pos.longitude == position.longitude && pos.latitude == position.latitude)
            {
                if(searchTime == 0)
                    temp.add(events.get(i));
                else if(searchTime == 1 && events.get(i).getStartTime() < (time + weekInMilli))
                    temp.add(events.get(i));
                else if(searchTime == 3 && events.get(i).getStartTime() < (time + dayInMilli))
                    temp.add(events.get(i));
            }
        }
        return temp;
    }

    public ArrayList<Event> getSmartEvents()
    {
        if(mainUser == null)
            return null;
        ArrayList<Pair<Event,Integer>> tempEvents = new ArrayList<>(10);
        Iterator<Map.Entry<String,Integer>> iter = mainUser.getLocationsPreference().entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<String,Integer> entry = iter.next();
            Event temp = getMostPopularEvent(entry.getKey());
            if(temp != null) {
                if (tempEvents.size() == 0)
                    tempEvents.add(new Pair<>(temp, entry.getValue()));
                else {
                    for (int i = 0; i < tempEvents.size(); i++) {
                        if (tempEvents.get(i).second < entry.getValue())
                            tempEvents.add(new Pair<>(temp, entry.getValue()));
                    }
                }
            }
        }
        ArrayList<Event> temp = new ArrayList();
        for(int i = 0; i < tempEvents.size(); i++)
        {
            temp.add(tempEvents.get(i).first);
        }
        return temp;
    }

    public Event getMostPopularEvent(String type)
    {
        Event tempEvent = null;
        for(int i = 0; i < events.size();i++)
        {
            if(tempEvent == null && events.get(i).getEventType().equals(type))
            {
                tempEvent = events.get(i);
            }
            else if(events.get(i).getEventType().equals(type) && events.get(i).getPopularity() > tempEvent.getPopularity())
            {
                tempEvent = events.get(i);
            }
        }
        return tempEvent;
    }


    public void addUser(Profile profile)
    {
        root = FirebaseDatabase.getInstance().getReference().child("Profiles").child(profile.getUsername());
        root.setValue(profile);
        getUser(profile.getUsername(),profile.getPassword());
    }

    public void getUser(String username, final String password)
    {

        root = FirebaseDatabase.getInstance().getReference().child("Profiles").child(username);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if(profile.getPassword().equals(password)) {
                        mainUser = profile;
                        getFevents();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFevents()
    {
        if(mainUser == null || mainUser.getFavEvents().size() == 0)
            return;
        Iterator<String> iter = mainUser.getFavEvents().keySet().iterator();
        followedEvents.clear();
        while(iter.hasNext())
        {
            root = FirebaseDatabase.getInstance().getReference().child("Events").child(iter.next());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    followedEvents.add(dataSnapshot.getValue(Event.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void addKeys(int amount)
    {
        numberOfKeys = amount;
        if(numberOfKeys == 0)
        {
            searching = false;
            return;
        }
        Iterator<String> keys = this.keys.keySet().iterator();
        while(keys.hasNext()) {
            root = FirebaseDatabase.getInstance().getReference().child("Events");
            root = root.child(keys.next());
            root.addListenerForSingleValueEvent(lis);
        }
    }

    private void addValue(Event e){
        tempEvents.add(e);
        keysAddedCurrently++;
        if(numberOfKeys == keysAddedCurrently) {
            events = tempEvents;
            tempEvents = new ArrayList<>();
            searching = false;
            returnSortedEvents();
        }
    }

    private void returnSortedEvents()
    {

        Long time = (System.currentTimeMillis());
        HashMap<String,Event> eventsSorted = new HashMap<>();
        for(int i = 0; i < events.size(); i++)
        {
            String temp = events.get(i).getPosition().toString();
            if( events.get(i).getEndTime() < time)
            {
                removeEvent(events.get(i).getKey());
            }
            else if(searchTime == 0 && (!eventsSorted.containsKey(temp) || eventsSorted.get(temp).getPopularity() < events.get(i).getPopularity()))
            {
                eventsSorted.put(temp,events.get(i));
            }
            else if(searchTime == 1 && events.get(i).getStartTime() < (time + weekInMilli) &&(!eventsSorted.containsKey(temp) || eventsSorted.get(temp).getPopularity() < events.get(i).getPopularity()))
            {
                eventsSorted.put(temp,events.get(i));
            }
            else if(searchTime == 2 && events.get(i).getStartTime() < (time + dayInMilli) &&(!eventsSorted.containsKey(temp) || eventsSorted.get(temp).getPopularity() < events.get(i).getPopularity()))
            {
                eventsSorted.put(temp,events.get(i));
            }
        }
        eventListener.getEvents(eventsSorted.values().toArray(new Event[0]));
    }

    private void removeEvent(String key)
    {
        root = FirebaseDatabase.getInstance().getReference().child("Event_Locations").child(key);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        root = FirebaseDatabase.getInstance().getReference().child("Events").child(key);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class GeoListener implements GeoQueryEventListener
    {
        private int numEvents = 0;

        @Override
        public void onKeyEntered(String key, GeoLocation location)
        {
            tempKeys.put(key,location);
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
            keys = tempKeys;
            tempKeys = new HashMap<>();
            addKeys(numEvents);
        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }

    }

    private class DataListener implements ValueEventListener{
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue() != null) {
                Event e = dataSnapshot.getValue(Event.class);
                GeoLocation temp = keys.get(dataSnapshot.getKey());
                e.setLatlng(new LatLng(temp.latitude, temp.longitude));
                e.setKey(dataSnapshot.getKey());
                addValue(e);
            }
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
    }

    public boolean isSearching()
    {
        return searching;
    }



}