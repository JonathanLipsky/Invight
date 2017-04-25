package com.example.lokid.projectfalcon;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Dillon on 4/25/17.
 */

public class Profile {
    private String username;
    private String password;
    private Map<String,Integer> favEvents;
    private Map<String,String> favLocations;

    public Profile()
    {
        favEvents = new HashMap<>();
        favLocations = new HashMap<>();
    }

    public Profile(String username,String password)
    {
        favEvents = new HashMap<>();
        favLocations = new HashMap<>();
        this.username = username;
        this.password = password;
    }

    public Profile(String username,String password, Map<String,Integer> favEvents, Map<String,String> favLocations)
    {
        favEvents = new HashMap<>();
        favLocations = new HashMap<>();
        this.username = username;
        this.password = password;
        this.favEvents = favEvents;
        this.favLocations = favLocations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Integer> getFavEvents() {
        return favEvents;
    }

    public void setFavEvents(Map<String, Integer> favEvents) {
        this.favEvents = favEvents;
    }

    public Map<String, String> getFavLocations() {
        return favLocations;
    }

    public void setFavLocations(Map<String, String> favLocations) {
        this.favLocations = favLocations;
    }
}
