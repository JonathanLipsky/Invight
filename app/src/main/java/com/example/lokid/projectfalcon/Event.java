package com.example.lokid.projectfalcon;

import android.icu.util.Calendar;

import java.util.Date;

/**
 * Created by lokid on 3/9/2017.
 */

public class Event {

    private String eventTitle;
    private Calendar eventDate;
    private Calendar eventTime;
    private String eventDiscription;



    public Event(String title){
        this.eventTitle = title;
    }

    public Event(String title, Calendar date){
        this(title);
        this.eventDate = date;
    }

    public Event(String title, Calendar date,String eventDiscription){
        this(title,date);
        this.eventDiscription = eventDiscription;
    }

    public Event(String title, Calendar date,String eventDiscription, Calendar time){
        this(title,date,eventDiscription);
        this.eventTime = time;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public Calendar getEventTime() {
        return eventTime;
    }

    public void setEventTime(Calendar eventTime) {
        this.eventTime = eventTime;
    }
    

    public String getEventDiscription() {
        return eventDiscription;
    }

    public void setEventDiscription(String eventDiscription) {
        this.eventDiscription = eventDiscription;
    }
}
