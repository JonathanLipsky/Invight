package com.example.lokid.projectfalcon;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.EXTRA_SUBJECT;


public class CardFragment extends Fragment {

    ArrayList<Event> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        int type = bundle.getInt("type");
        if(type == 0) {
            LatLng pos = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
            listitems = MainActivity.getDatabaseReference().getEvents(pos);

            Event sponsor = new Event();
            sponsor.setTitle("Bannana Stand");
            sponsor.setEventType("Community");
            sponsor.setIsPromoted(true);
            listitems.add(0,sponsor);
            //String eventsAddress = bundle.getString("address");
        }
        else if(type == 1){
            listitems = MainActivity.getDatabaseReference().getFollowedEvents();
        }
        else if(type == 2)
        {
            listitems = MainActivity.getDatabaseReference().getSmartEvents();
        }
        if(listitems == null)
            listitems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Event> list;

        public MyAdapter(ArrayList<Event> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.item=list.get(position);
            holder.titleTextView.setText(list.get(position).getTitle());
            if (list.get(position).isPromoted()) {
                holder.sponsorText.setText("*Sponsored");
                holder.sponsorBar.setBackgroundColor(Color.parseColor("#FFFFBB33"));
            }
            holder.coverImageView.setImageResource(getEventType(list.get(position).getEventType()));
            holder.coverImageView.setTag(getEventType(list.get(position).getEventType()));
            holder.likeImageView.setTag(R.drawable.ic_like);

        }

        private int getEventType(String event)
        {
            int eventName;
            switch(event)
            {
                case "Bar": eventName = R.drawable.bar_hdpi; break;
                case "Community": eventName = R.drawable.community_hdpi; break;
                case "Concert": eventName = R.drawable.concert_hdpi; break;
                case "Education": eventName = R.drawable.education_hdpi; break;
                case "Fund Raiser": eventName = R.drawable.fundraiser_hdpi; break;
                case "Get Together": eventName = R.drawable.get_together_hdpi; break;
                case "Kids": eventName = R.drawable.kids_hdpi; break;
                case "Party": eventName = R.drawable.party_hdpi; break;
                case "Political": eventName = R.drawable.political_hdpi; break;
                case "Sport": eventName = R.drawable.sport_hdpi; break;
                default: eventName = 0; break;
            }
            return eventName;
        }



       @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public TextView sponsorText;
        public LinearLayout sponsorBar;
        public Event item;

        private int getEventType(String event)
        {
            int eventName;
            switch(event)
            {
                case "Bar": eventName = R.drawable.bar_hdpi; break;
                case "Community": eventName = R.drawable.community_hdpi; break;
                case "Concert": eventName = R.drawable.concert_hdpi; break;
                case "Education": eventName = R.drawable.education_hdpi; break;
                case "Fund Raiser": eventName = R.drawable.fundraiser_hdpi; break;
                case "Get Together": eventName = R.drawable.get_together_hdpi; break;
                case "Kids": eventName = R.drawable.kids_hdpi; break;
                case "Party": eventName = R.drawable.party_hdpi; break;
                case "Political": eventName = R.drawable.political_hdpi; break;
                case "Sport": eventName = R.drawable.sport_hdpi; break;
                default: eventName = 0; break;
            }
            return eventName;
        }
        private int EventTypePic(String event)
        {
            int eventName;
            switch(event)
            {
                case "Bar": eventName = R.drawable.bar_event; break;
                case "Community": eventName = R.drawable.community_event; break;
                case "Concert": eventName = R.drawable.concert_event; break;
                case "Education": eventName = R.drawable.school_event; break;
                case "Fund Raiser": eventName = R.drawable.fundraising_event; break;
                case "Get Together": eventName = R.drawable.get_together_event; break;
                case "Kids": eventName = R.drawable.kids_event; break;
                case "Party": eventName = R.drawable.party_event; break;
                case "Political": eventName = R.drawable.political_event; break;
                case "Sport": eventName = R.drawable.sporting_event; break;
                default: eventName = R.drawable.get_together_event; break;
            }

            return eventName;
        }

        public MyViewHolder(View v) {
            super(v);
            sponsorText = (TextView)v.findViewById(R.id.sponsorText);
            sponsorBar = (LinearLayout) v.findViewById(R.id.bottomSponsorbar);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                        if( id == R.drawable.ic_like){

                            MainActivity.getDatabaseReference().eventLiked(item,true);
                            likeImageView.setTag(R.drawable.ic_liked);
                            likeImageView.setImageResource(R.drawable.ic_liked);

                            Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites", Toast.LENGTH_SHORT).show();

                        }else{
                            MainActivity.getDatabaseReference().eventLiked(item,false);
                            likeImageView.setTag(R.drawable.ic_like);
                            likeImageView.setImageResource(R.drawable.ic_like);
                            Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites", Toast.LENGTH_SHORT).show();


                        }

                }
            });
            coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.event_page, (ViewGroup) v.findViewById(R.id.eventPageLayout), false);
                    final PopupWindow pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                    ((TextView) layout.findViewById(R.id.title_box)).setText(item.getTitle());
                    ((TextView) layout.findViewById(R.id.Description_Box)).setText(item.getSnippet());
                    ((TextView) layout.findViewById(R.id.event_type_box)).setText("Event Type:    " + item.getEventType());
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(item.getStartTime());
                    ((TextView) layout.findViewById(R.id.start_time_box)).setText("Start Time :  " + cal.getTime());
                    cal.setTimeInMillis(item.getEndTime());
                    ((TextView) layout.findViewById(R.id.end_time_box)).setText("End Time :  " + cal.getTime());
                    String type = item.getEventType();

                    ((ImageView) layout.findViewById(R.id.eventPic)).setImageResource(getEventType(type));
                    pwindo.setAnimationStyle(R.style.Animation);
                    pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });




            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));

                    String text = "You have been invited to ";
                    text += item.getTitle();
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);

                   // shareIntent.putExtra(EXTRA_SUBJECT, text);

                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                }
            });



        }
    }

}
