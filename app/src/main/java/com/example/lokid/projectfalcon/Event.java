package com.example.lokid.projectfalcon;

/**
 * This class represents an event.
 */

public class Event {

    private String eventTitle;
    private double longitude;
    private double latitude;
    private long startTime;
    private long endTime;
    private String eventDescription;
    private String photoReference;
    private String host;
    private int popularity;
    private String eventType;
    private boolean isPublicEvent;

    public Event() {}

    public Event(double longitude, double latitude,long startTime,long endTime,boolean isPublicEvent){
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPublicEvent = isPublicEvent;
    }

    public Event( double longitude, double latitude, long startTime, long endTime, boolean isPublicEvent,String eventTitle, String eventDescription, String host, int popularity, String eventType) {
        this.eventTitle = eventTitle;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventDescription = eventDescription;
        this.host = host;
        this.popularity = popularity;
        this.eventType = eventType;
        this.isPublicEvent = isPublicEvent;
    }

    public boolean isPublicEvent() {return isPublicEvent;}

    public void setPublicEvent(boolean publicEvent) {this.isPublicEvent = publicEvent;}

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public String getPhotoReference() {return photoReference;}

    public void setPhotoReference(String photoReference) {this.photoReference = photoReference;}

    public String getHost() {return host;}

    public void setHost(String host) {this.host = host;}

    public int getPopularity() {return popularity;}

    public void setPopularity(int popularity) {this.popularity = popularity;}

    public String getEventType() {return eventType;}

    public void setEventType(String eventType) {this.eventType = eventType;}

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getEventDiscription() {
        return eventDescription;
    }

    public void setEventDiscription(String eventDescription) {this.eventDescription = eventDescription;}
}
