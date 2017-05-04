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
    private Map<String,Integer> locationsPreference;

    public Profile()
    {
        favEvents = new HashMap<>();
        locationsPreference = new HashMap<>();
    }

    public Profile(String username,String password)
    {
        super();
        favEvents = new HashMap<>();
        locationsPreference = new HashMap<>();
        this.username = username;
        this.password = password;
    }

    public Profile(String username,String password, HashMap<String,Integer> favEvents, HashMap<String,Integer> locationsPreference)
    {
        this.username = username;
        this.password = password;
        this.favEvents = favEvents;
        this.locationsPreference = locationsPreference;
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

    public Map<String, Integer> getLocationsPreference() {
        return locationsPreference;
    }

    public void setLocationsPreference(Map<String, Integer> locationsPreference) {
        this.locationsPreference = locationsPreference;
    }
}
