package com.example.lokid.projectfalcon;

import java.util.Locale;

/**
 * This class is made to link the data base to the project, includes ways to save
 * and retrieve data.
 */

public class DatabaseHandler {

    /**
     * This method adds an event to the database.
     * @return true if adding the event to the database was successful.
     */
    public static boolean addEvent(Event e)
    {
        //code here
        return true;
    }

    /**
     * This method returns the most popular event at each location found within a day span
     */
    public static Event[] retriveEventsDaySpan(double topLeftLong, double topRightlat,
                                               double bottemRightLong, double bottemRightLat)
    {
        Event[] e = new Event[2];
        e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        e[1] = new Event(37.419544,-122.0746407,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
        //first one is google plex lat and long seccond is near.
        return e;
    }

    /**
     * This method returns the most popular event at each location found within a week span
     */
    public static Event[] retriveEventsWeekSpan(double topLeftLong, double topRightlat,
                                                double bottemRightLong, double bottemRightLat)
    {
        Event[] e = new Event[2];
        e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        e[1] = new Event(37.419544,-122.0746407,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
        return e;
    }

    /**
     * This method returns the most popular event at each location found within no span
     */
    public static Event[] retriveEventsNoSpan(double topLeftLong, double topRightlat,
                                              double bottemRightLong, double bottemRightLat)
    {
        Event[] e = new Event[2];
        e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        e[1] = new Event(37.419544,-122.0746407,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
        return e;
    }

    /**
     * This method return all the events at one location
     */
    public static Event[] getEventsAtLocation(double longitude, double latitude)
    {
        Event[] e = new Event[2];
        e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        e[1] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
        return e;
    }

}
