package com.example.lokid.projectfalcon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.lokid.projectfalcon.DatabaseHandler.RetrieveEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Calendar;
//import static com.example.lokid.projectfalcon.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        ConnectionCallbacks,
        LocationListener,
        OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnMarkerClickListener,ClusterManager.OnClusterItemClickListener<Event>,RetrieveEventListener{

    private final int REQUEST_CODE_PLACEPICKER = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient mGoogleApiClientPlaces;
    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private ClusterManager<Event> mClusterManager;
    private static DatabaseHandler database;
    private LatLng mapCenter;
    private LatLng mapCorner;
    private ViewFlipper viewFlipper;
    private View tutorialLayout;
    private Button next;
    private PopupWindow mPopupWindow;
    private Activity activity = this;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    SupportMapFragment mapFragment;
    android.support.v4.app.FragmentManager sfm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            Toast.makeText(this, "First Time", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();

        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        mapFragment = SupportMapFragment.newInstance();
        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();

        //mGoogleApiClientPlaces.connect();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                //Log.i(TAG, "Place: " + place.getName());

                displayPlace(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfm.beginTransaction().hide(mapFragment).commit();
                Fragment fragment = new CardFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type",2);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();
               // onClusterItemClick(database.getSmartEvent());
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapFragment.getMapAsync(this);

        mContext = getApplicationContext();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.map);

        sfm = getSupportFragmentManager();
        sfm.beginTransaction().add(R.id.mapFrame, mapFragment).commit();



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        activity = this;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);                            //TODO do an overirde to popup event page

        //sets up the cluster manager
        mClusterManager = new ClusterManager<>(mContext,mMap);
        mClusterManager.setRenderer(new EventRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(this);
        //sets up database
        database = new DatabaseHandler();
        database.addEventLister(this);
        mapCenter = new LatLng(0,0);
        mapCorner = new LatLng(0,0);
        reDrawPins(true);
        database.getUser("dillon","odonnell");

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


                                           @Override
                                           public void onMapLongClick(LatLng latLng) {
                                               startPlacePickerActivity(latLng);
                                           }
                                       }
        );
    }

    View.OnClickListener viewClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    };

    private void startPlacePickerActivity(LatLng tapSpot) {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();


        intentBuilder.setLatLngBounds(toBounds(tapSpot, 400.00));
        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String location_number;
    private void displayPlace(Place place) {
        if (place == null)
            return;
        String location_name = "";
        String location_address = "";
        location_number = "";

        String content = "";
        if (!TextUtils.isEmpty(place.getName())) {
            location_name = place.getName() + "\n";
        }
        if (!TextUtils.isEmpty(place.getAddress())) {
            location_address = place.getAddress() + "\n";
        }
        if (!TextUtils.isEmpty(place.getPhoneNumber())) {
            location_number = (String) place.getPhoneNumber();
        }




        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.location, (ViewGroup) findViewById(R.id.popup_location), false);

        final PopupWindow pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //setContentView(R.layout.location);

        // mPopupWindow = new PopupWindow(viewPopUp, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        //placePhotosAsync(place);
        
        TextView mTextView2 = (TextView) layout.findViewById(R.id.textLocationAddress);
        mTextView2.setText(location_address);

        TextView mTextView3 = (TextView) layout.findViewById(R.id.textLocationNumber);

        SpannableString phone = new SpannableString(location_number);
        phone.setSpan(new UnderlineSpan(), 0, phone.length(), 0);
        mTextView3.setText(phone);

        mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", location_number, null));
                startActivity(intent);
            }
        });

        TextView mTextView = (TextView) layout.findViewById(R.id.textLocationName);
        mTextView.setText(location_name);

        final LatLng position = place.getLatLng();
        final String fLocationAdress = location_address;

        Button button = (Button) layout.findViewById(R.id.buttonCreateEventPopUp);
        Button btnList = (Button)layout.findViewById(R.id.buttonEvents);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();
                sfm.beginTransaction().hide(mapFragment).commit();
                Fragment fragment = new create_event_fragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", position.latitude);
                bundle.putDouble("lng", position.longitude);
                bundle.putInt("type",0);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();

            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();
                sfm.beginTransaction().hide(mapFragment).commit();
                Fragment fragment = new CardFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", position.latitude);
                bundle.putDouble("lng", position.longitude);
                bundle.putString("address",fLocationAdress);
                bundle.putInt("type",0);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();
            }
        });

        pwindo.setAnimationStyle(R.style.Animation);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        //mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }
            mImageView.setImageBitmap(placePhotoResult.getBitmap());
        }
    };


  private ImageView mImageView;

    private void placePhotosAsync(Place place) {

        mImageView = (ImageView)findViewById(R.id.mImageView);
        final String placeId = place.getId(); // Australian Cruise Group
        //
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(mGoogleApiClient, mImageView.getWidth(),
                                            mImageView.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displayPlace(PlacePicker.getPlace(data, this));
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((ConnectionCallbacks) this)
                .addOnConnectionFailedListener((OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_list, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.hour_24) {
            database.setSearchTime(2);
            reDrawPins(true);
            return true;
        }
        if (id == R.id.days_7) {
            database.setSearchTime(1);
            reDrawPins(true);
            return true;
        }
        if (id == R.id.all_time) {
            database.setSearchTime(0);
            reDrawPins(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;

        int id = item.getItemId();

        sfm.beginTransaction().hide(mapFragment).commit();


        if (id == R.id.nav_profile) {

            fragment = new RegistrationFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();

        } else if (id == R.id.nav_settings) {

            fragment = new SettingsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();

        } else if (id == R.id.nav_home) {
            fragment = new BlankFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();
            sfm.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .show(mapFragment).commit();
        } else if (id == R.id.nav_favorites) {

            fragment = new CardFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",1);
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }



    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        VisibleRegion vRegion = mMap.getProjection().getVisibleRegion();
        LatLng southwest = vRegion.latLngBounds.southwest;
        LatLng northeast = vRegion.latLngBounds.northeast;

        Double leftLat = southwest.latitude;
        Double rightLat = northeast.latitude;
        Double leftLong = southwest.longitude;
        Double rightLogn = northeast.longitude;

    }

    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    @Override
    public boolean onClusterItemClick(Event item) {
        // Does nothing, but you could go into the user's profile page, for example.

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.event_page, (ViewGroup) findViewById(R.id.eventPageLayout), false);
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


        ((ImageView) layout.findViewById(R.id.eventPic)).setImageResource(EventTypePic(type));

        final LatLng position = item.getPosition();
        Button button = (Button) layout.findViewById(R.id.event_location_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();
                sfm.beginTransaction().hide(mapFragment).commit();
                Fragment fragment = new CardFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", position.latitude);
                bundle.putDouble("lng", position.longitude);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();
            }
        });

        pwindo.setAnimationStyle(R.style.Animation);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        return false;
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

    private int getEventType(String event)
    {
        int eventName;
        switch(event)
        {
            case "Bar": eventName = R.drawable.bar; break;
            case "Community": eventName = R.drawable.community; break;
            case "Concert": eventName = R.drawable.concert; break;
            case "Education": eventName = R.drawable.education; break;
            case "Fund Raiser": eventName = R.drawable.fundraiser; break;
            case "Get Together": eventName = R.drawable.get_together; break;
            case "Kids": eventName = R.drawable.kids; break;
            case "Party": eventName = R.drawable.party; break;
            case "Political": eventName = R.drawable.political; break;
            case "Sport": eventName = R.drawable.sport; break;
            default: eventName = 0; break;
        }
        return eventName;
    }

    public GoogleMap getMap()
    {
        return mMap;
    }

    @Override
    public void getEvents(Event[] events) {
        mClusterManager.clearItems();
        for(Event e: events)
        {
            mClusterManager.addItem(e);
        }
        mClusterManager.cluster();
    }

    public void reDrawPins(boolean override)
    {
        //redraws pins only if the map has changed
        if(database.isSearching())
            return;
        VisibleRegion vRegion = mMap.getProjection().getVisibleRegion();
        LatLng cen = vRegion.latLngBounds.getCenter();
        LatLng corn = vRegion.latLngBounds.northeast;
        if( override || corn.longitude != mapCorner.longitude || corn.latitude != mapCorner.latitude || cen.latitude != mapCenter.latitude || cen.longitude != mapCenter.longitude) {
            Location center = new Location("");
            Location corner = new Location("");
            center.setLatitude(cen.latitude);
            center.setLongitude(cen.longitude);
            corner.setLatitude(corn.latitude);
            corner.setLongitude(corn.longitude);
            database.retrieveEvents(center, corner);
            mapCenter = cen;
            mapCorner = corn;
        }
    }

    private class EventRenderer extends DefaultClusterRenderer<Event> implements GoogleMap.OnCameraIdleListener{


        public EventRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Event e, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            int type = getEventType(e.getEventType());
            if(type == 0) {
                type = R.drawable.other;
            }
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        type);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }


        @Override
        public void onCameraIdle() {
            reDrawPins(false);
        }



    }
    public static DatabaseHandler getDatabaseReference()
    {
        return database;
    }
}
