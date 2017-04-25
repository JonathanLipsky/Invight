/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lokid.projectfalcon;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Event implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private long startTime;
    private long endTime;
    private String photoReference;
    private String host;
    private String key;
    private int popularity;
    private int eventType;

    public Event()
    {
        //must have for DB.
    }

    public Event(LatLng pos) {
        mPosition = pos;
        mTitle = null;
        mSnippet = null;
    }

    public Event(LatLng pos, String title, String snippet) {
        mPosition = pos;
        mTitle = title;
        mSnippet = snippet;
    }

    public Event( LatLng pos, long startTime, long endTime,String eventTitle, String eventDescription, String host, int popularity, int eventType) {

        this(pos, eventTitle,eventDescription);
        this.startTime = startTime;
        this.endTime = endTime;
        this.host = host;
        this.popularity = popularity;
        this.eventType = eventType;
        this.photoReference = "";
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

   // @Override
    public String getTitle() { return mTitle; }

   // @Override
    public String getSnippet() { return mSnippet; }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
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

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public double getLong(){return mPosition.longitude;}

    public double getLat(){return mPosition.latitude;}

    public void setLatlng(LatLng pos){mPosition = pos;}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}
}
