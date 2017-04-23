package com.example.lokid.projectfalcon;

import com.google.android.gms.maps.model.LatLng;
/*import com.google.api.client.util.DateTime;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.TableId;*/
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is made to link the data base to the project, includes ways to save
 * and retrieve data.
 */

public class DatabaseHandler {

    private static DatabaseReference root;
    /**
     * This method adds an event to the database.
     * @return true if adding the event to the database was successful.
     */
    public static boolean addEvent(Event e)
    {
        //code to send event to db
       // BigQuery bigquery = BigQueryOptions.defaultInstance().service();
       /* BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        TableId tableId = TableId.of("Invight","Event");
        Map<String,Object> rowContent = new HashMap<>();
        rowContent.put("eventName",e.getTitle());
        rowContent.put("long", e.getLong());
        rowContent.put("lat", e.getLat());
        rowContent.put("startTime", new DateTime(e.getStartTime()));
        rowContent.put("endTime", new DateTime(e.getEndTime()));
        rowContent.put("popularity",e.getPopularity());
        rowContent.put("eventType", e.getEventType());
        rowContent.put("description",e.getSnippet());
        rowContent.put("host",e.getHost());
        rowContent.put("photo",e.getPhotoReference());
        rowContent.put("eventID","ID Here");

        InsertAllResponse response = bigQuery.insertAll(InsertAllRequest.newBuilder(tableId)
            .addRow(rowContent)
            .build());

        if(response.hasErrors())
        {
            //do something
            return false;
        }*/

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
        // e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        //  e[1] = new Event(37.419544,-122.0746407,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
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
        //e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,true,"TestEvent1","just a test Event","Dillon",0,"Festival");
        // e[1] = new Event(37.419544,-122.0746407,1491269439, 1491279439,true,"TestEvent2","just another test Event","Dillon",2,"Festival");
        return e;
    }

    /**
     * This method returns the most popular event at each location found within no span
     */
    public static Event[] retrieveEventsNoSpan(LatLng topLeft, LatLng topRight)
    {
        Event[] e = new Event[10];
        e[0] = new Event(37.418544,-122.0746307,1491369439, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.bar);
        e[1] = new Event(37.419544,-122.0747307,1491469449, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.community);
        e[2] = new Event(37.417544,-122.0748307,1491569459, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.concert);
        e[3] = new Event(37.416544,-122.0749307,1491669439, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.education);
        e[4] = new Event(37.415544,-122.0743307,1491769539, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.fundraiser);
        e[5] = new Event(37.414544,-122.0742307,1491869639, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.get_together);
        e[6] = new Event(37.413544,-122.0742307,1491869639, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.kids);
        e[7] = new Event(37.412544,-122.0742307,1491869639, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.party);
        e[8] = new Event(37.411544,-122.0742307,1491869639, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.political);
        e[9] = new Event(37.409544,-122.0742307,1491869639, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.sport);
        return e;
    }

    /**
     * This method return all the events at one location
     */
    public static Event[] getEventsAtLocation(double longitude, double latitude)
    {
        Event[] e = new Event[2];
        e[0] = new Event(37.419544,-122.0746307,1491269439, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.bar);
        e[1] = new Event(37.419544,-122.0746307,1491269439, 1491279439,"TestEvent2","just another test Event","Dillon",2,R.drawable.community);
        return e;
    }

}
