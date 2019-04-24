package com.example.picar.activities;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.picar.R;
import com.example.picar.database.AppDatabase;
import com.example.picar.database.entity.User;
import com.example.picar.directionHelpers.FetchUrl;
import com.example.picar.directionHelpers.TaskLoadedCallback;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.database.entity.Position;
import com.example.picar.retrofit.http_request.User_http_request;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, User_http_request.UserHttpError
        , User_http_request.UserHttpResponse {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWNhYmE2YmUwOWJhYjkyN2QxMWIwMTRhIiwiaWF0IjoxNTU1MzM2MDkwfQ.Ivk36K7629DVF_oSCeDqNO_N_DhDS8n37_mN09qmHXE";


    private PutPositionTask putPositionTask = null;

   // private GetTransitTask getTransitTask = null
//    private String GEOFENCE_REQ_ID = "myGeofence";
//    private PendingIntent geofencePendingI;
    // The entry points to the Places API.
    //private GeoDataClient mGeoDataClient;
    //private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private Address mCurrentAddress;
    private Address mDestinationAddress;

    private MarkerOptions mCurrentMarkerOptions;
    private MarkerOptions mDestinationMarkerOptions;

    private Polyline currentPolyline;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    EditText locationSearch;

    private PiCarApi api;

    private String TYPE;

    private User user;

    Button btn_rides;
    Button btn_location;
    Button btn_destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasExtra("type")){
            TYPE = intent.getStringExtra("type");
        }

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PiCarApi.class);


        setContentView(R.layout.activity_maps);

//        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this);

//        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_location =  findViewById(R.id.search_button_location);
        btn_destination = findViewById(R.id.search_button_destination);
        btn_rides = findViewById(R.id.search_button_ride);

        final EditText ed_destination = findViewById(R.id.editText_destination);
        final EditText ed_location =  findViewById(R.id.editText_location);

        user = (User) AppDatabase.getInstance(this).userDao().getUser();

        btn_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation(ed_location);
                setDestinationLocation(ed_destination);
                if(mCurrentAddress != null && mDestinationAddress != null){
                    mCurrentMarkerOptions = new MarkerOptions().position(new LatLng(mCurrentAddress.getLatitude(), mCurrentAddress.getLongitude()));
                    mDestinationMarkerOptions = new MarkerOptions().position(new LatLng(mDestinationAddress.getLatitude(), mDestinationAddress.getLongitude()));
                    ArrayList<MarkerOptions> markers = new ArrayList<>();
                    markers.add(mCurrentMarkerOptions);
                    markers.add(mDestinationMarkerOptions);
                    new FetchUrl(MapsActivity.this).execute(getUrl(mCurrentMarkerOptions.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");


                    //show all marker on the map
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (MarkerOptions marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 500; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.animateCamera(cu);

                   String currentLocationId = AppDatabase.getInstance(v.getContext()).userDao().getUserCurrentPositionId();
                   String destinationId = AppDatabase.getInstance(v.getContext()).userDao().getDestinationId();

                    putPositionTask = new PutPositionTask(currentLocationId, mCurrentMarkerOptions);
                    putPositionTask.execute((Void) null);
                    putPositionTask = new PutPositionTask(destinationId, mDestinationMarkerOptions);
                    putPositionTask.execute((Void) null);

                }
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_destination!=null){
                    setCurrentLocation(ed_location);
                }
            }
        });

        btn_rides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //if driver
                if(user.isDriver()){
                    Handler handler = new Handler();
                    int delay = 5000; //milliseconds

                    handler.postDelayed(new Runnable(){
                        public void run(){
                            String currentLocationId = AppDatabase.getInstance(getApplicationContext()).userDao().getUserCurrentPositionId();
                            MarkerOptions newCurrentPosition = new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                            putPositionTask = new PutPositionTask(currentLocationId, newCurrentPosition);
                            new FetchUrl(MapsActivity.this).execute(getUrl(newCurrentPosition.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");
                            putPositionTask.execute((Void) null);
                            handler.postDelayed(this, delay);
                        }
                    }, delay);


                }
                else {
                    Intent i = new Intent(MapsActivity.this,CardViewActivity.class);
                    startActivity(i);
                }

            }
        });

        if(TYPE.equals("Driver")){
            btn_rides.setText("Start ride");
        }
    }

    private void updatePosition(String id, MarkerOptions marker) {
        Position position = new Position(marker.getPosition().latitude, marker.getPosition().longitude);

        Call<Position> call = api.putPosition(token,id, position);

        call.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        locationSearch = (EditText) findViewById(R.id.editText_location);


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void setCurrentLocation(View view){
        mCurrentAddress = onMapSearch((EditText) view);
    }

    public void setDestinationLocation(View view){
        mDestinationAddress = onMapSearch((EditText) view);
    }


    public Address onMapSearch(EditText editText) {
        EditText locationSearch = (EditText) editText;
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;


        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            return address;
        }
        return null;
    }

    public String getAddress(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);


            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            locationSearch.setText(getAddress(getApplicationContext(), mLastKnownLocation));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            //showCurrentPlace();
        }
        return true;
    }


    public void confirm(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
//                        MapsActivity.this.startActivity(MapsActivity.this, SecondActivity.class);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Search ride destination near : " + mDestinationAddress.getAddressLine(0));
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }

    private String getUrl(LatLng current, LatLng destination, String directionMode){
        String str_origin = "origin=" + current.latitude + "," + current.longitude;
        // Destination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;

    }

    public class PutPositionTask extends AsyncTask<Void, Void, Boolean> {


        private final String mId;
        //        private final String mPosition;
        private final MarkerOptions mMarkerOptions;

        PutPositionTask(String id, MarkerOptions markerOptions) {
            mId = id;
            mMarkerOptions = markerOptions;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(MapsActivity.this);
            Position position = new Position(mMarkerOptions.getPosition().latitude, mMarkerOptions.getPosition().longitude);

            request.PutPosition(token, mId, position);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            putPositionTask = null;
            //showProgress(false);

            if (success) {
                // finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                // mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            putPositionTask = null;
            //showProgress(false);
        }


    }

//    public class GetTransit extends AsyncTask<Void, Void, Boolean> {
//
//
//
//
//        PutPositionTask(String id, MarkerOptions markerOptions) {
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            User_http_request request = new User_http_request(MapsActivity.this);
//            Position position = new Position(mMarkerOptions.getPosition().latitude, mMarkerOptions.getPosition().longitude);
//
//            request.PutPosition(token, mId, position);
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            putPositionTask = null;
//            //showProgress(false);
//
//            if (success) {
//                // finish();
//            } else {
//                //mPasswordView.setError(getString(R.string.error_incorrect_password));
//                // mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            putPositionTask = null;
//            //showProgress(false);
//        }
//
//
//    }


    public void updateMapAsyncTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            String currentLocationId = AppDatabase.getInstance(getApplicationContext()).userDao().getUserCurrentPositionId();
                            MarkerOptions newCurrentPosition = new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                            putPositionTask = new PutPositionTask(currentLocationId, newCurrentPosition);
                            new FetchUrl(MapsActivity.this).execute(getUrl(newCurrentPosition.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");
                            putPositionTask.execute((Void) null);


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }

    public class UpdatePositionService extends Service {
        Handler handler;
        Runnable test;
        public UpdatePositionService() {
            handler = new Handler();
            test = new Runnable() {
                @Override
                public void run() {
                    String currentLocationId = AppDatabase.getInstance(getApplicationContext()).userDao().getUserCurrentPositionId();
                    MarkerOptions newCurrentPosition = new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                    putPositionTask = new PutPositionTask(currentLocationId, newCurrentPosition);
                    new FetchUrl(MapsActivity.this).execute(getUrl(newCurrentPosition.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");
                    putPositionTask.execute((Void) null);
                    handler.postDelayed(test, 4000);
                }
            };

            handler.postDelayed(test, 0);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }





}
