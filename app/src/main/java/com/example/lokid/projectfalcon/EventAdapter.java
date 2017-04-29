package com.example.lokid.projectfalcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by lokid on 3/13/2017.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event>items;
    private Context adapterContext;

    public EventAdapter(Context context, ArrayList<Event> items) {
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup partent){
        View v = convertView;
        try{
            Event currentEvent = items.get(position);
            if(v==null){
                LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            ImageView eventType = (ImageView)v.findViewById(R.id.imageEventType);
            TextView eventName = (TextView)v.findViewById(R.id.textEventNameItem);
            TextView eventAddress = (TextView)v.findViewById(R.id.textEventAddress);
            ToggleButton favorite = (ToggleButton) v.findViewById(R.id.tbFavorite);
            eventName.setText(currentEvent.getTitle());
            //b.setVisibility(View.INVISIBLE);
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return v;
    }
}
