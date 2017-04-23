package com.example.lokid.projectfalcon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
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
import android.text.TextUtils;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
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
        OnMarkerClickListener,ClusterManager.OnClusterItemClickListener<Event>{

    private final int REQUEST_CODE_PLACEPICKER = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient mGoogleApiClientPlaces;
    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private ClusterManager<Event> mClusterManager;

    private PopupWindow mPopupWindow;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    SupportMapFragment mapFragment;
    android.support.v4.app.FragmentManager sfm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        mClusterManager = new ClusterManager<>(mContext,mMap);
        mClusterManager.setRenderer(new EventRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(this);
        Event[] events = DatabaseHandler.retrieveEventsNoSpan(mMap.getProjection().fromScreenLocation(new Point(0,0)),mMap.getProjection().fromScreenLocation(new Point(0,0)));
        for(Event e: events)
        {
            mClusterManager.addItem(e);
        }
        mClusterManager.cluster();

        if(!DatabaseHandler.addEvent(new Event(37.418544,-122.0746307,1491369439, 1491279439,"TestEvent1","just a test Event","Dillon",0,R.drawable.bar)))
            mClusterManager.clearItems();



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

    private void displayPlace(Place place) {
        if (place == null)
            return;
        Fragment fragment = null;
        String location_name = "";
        String location_address = "";
        String location_number = "";

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
        mTextView3.setText(location_number);

        TextView mTextView = (TextView) layout.findViewById(R.id.textLocationName);
        mTextView.setText(location_name);

        Button button = (Button) findViewById(R.id.buttonCreateEvent);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          // Perform action on click
//                                          Fragment fragment = new CreateFragment();
//                                          FragmentManager fragmentManager = getSupportFragmentManager();
//                                          fragmentManager.beginTransaction()
//                                                  .replace(R.id.content_event_list, fragment)
//                                                  .commit();
                                          Fragment fragment = new CreateFragment();
                                          FragmentManager manager = getSupportFragmentManager();
                                          manager.beginTransaction()
                                                  .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                                  .replace(R.id.content_event_list, fragment, fragment.getTag()).commit();




                                      }
                                  });

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
        if (id == R.id.action_settings) {
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
        ((TextView) layout.findViewById(R.id.event_type_box)).setText("Event Type:    " + getEventType(item.getEventType()));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(item.getStartTime());
        ((TextView) layout.findViewById(R.id.start_time_box)).setText("Start Time :  " + cal.getTime());
        cal.setTimeInMillis(item.getEndTime());
        ((TextView) layout.findViewById(R.id.end_time_box)).setText("End Time :  " + cal.getTime());

        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        return false;
    }

    public String getEventType(int event)
    {
        String eventName;
        switch(event)
        {
            case R.drawable.bar: eventName = "Bar"; break;
            case R.drawable.community: eventName = "Community"; break;
            case R.drawable.concert: eventName = "Concert"; break;
            case R.drawable.education: eventName = "Education"; break;
            case R.drawable.fundraiser: eventName = "Fundraiser"; break;
            case R.drawable.get_together: eventName = "Get Together"; break;
            case R.drawable.kids: eventName = "Kids"; break;
            case R.drawable.party: eventName = "Party"; break;
            case R.drawable.political: eventName = "Political"; break;
            case R.drawable.sport: eventName = "Sport"; break;
            default: eventName = "Other"; break;
        }
        return eventName;
    }

    public GoogleMap getMap()
    {
        return mMap;
    }

    private class EventRenderer extends DefaultClusterRenderer<Event> implements GoogleMap.OnCameraIdleListener{


        public EventRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Event e, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            if(e.getEventType() != 0) {
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        e.getEventType());
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            }
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }


        @Override
        public void onCameraIdle() {
            //mClusterManager.clearItems();
            //mClusterManager.addItem(new Event(37.429544,-122.0748307,1491369439, 1491279439,true,"Worked","just a test Event","Dillon",0,R.drawable.bar));
            //mClusterManager.cluster();
        }

    }
}
