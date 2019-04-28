package com.example.picar.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.picar.JSONParser;
import com.example.picar.Preferences;
import com.example.picar.R;
import com.example.picar.database.AppDatabase;
import com.example.picar.database.entity.Position;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.directionHelpers.FetchUrl;
import com.example.picar.directionHelpers.TaskLoadedCallback;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.http_request.User_http_request;
import com.example.picar.retrofit.model.DriverInfoForTransit;
import com.example.picar.retrofit.model.StatusInfo;
import com.example.picar.retrofit.model.StatusUpdateResponse;
import com.example.picar.retrofit.model.type_message.MessageUserEmailApproval;
import com.example.picar.retrofit.model.user_type.UserInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, User_http_request.UserHttpError, User_http_request.UserHttpResponse
{
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private String token = "";


    /*AsynTask*/
    private GetTransitWithDriveID getTransitWithDriveID = null;
    private GetPositionCurrent_IDPosition getPositionCurrent_idPosition = null;
    private PutPositionTask putPositionTask = null;
    private GetTransitByDriverIdTask getTransitByDriverIdTask = null;
    private UpdateStatusToValidatedTask updateStatusToValidatedTask = null;
    private UpdateStatusToRefusedTask updateStatusToRefusedTask = null;
    private GetPassagerCurrentPositionTask getPassagerCurrentPositionTask = null;
    private GetPassagerDestinationPositionTask getPassagerDestinationPositionTask = null;

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

    LinearLayout mainLayout;



    private MarkerOptions mCurrentMarkerOptions;
    private MarkerOptions mDestinationMarkerOptions;
    private MarkerOptions mDriverCurrentmarkerOptions = new MarkerOptions();
    private Marker mDriverCurrent;
    private MarkerOptions passagerCurrentMarkerOptions;
    private MarkerOptions passagerDestinationMarkerOptions;



    private Polyline currentPolyline;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    EditText locationSearch;

    private PiCarApi api;
    Context context;
    private String TYPE;
    private String validated = "";
    private String driver_id;

    Button btn_rides;
    Button btn_location;
    Button btn_destination;

    private Transit transit;
    private User user;
    private UserInfo passager;

    private boolean passagerTrouver = false;
    ArrayList<LatLng> waypoints = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        token = Preferences.getInstance(getBaseContext()).getString("Authorization");
        if (!token.equalsIgnoreCase("")) {
            token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWNhYmE2YmUwOWJhYjkyN2QxMWIwMTRhIiwiaWF0IjoxNTU1MzM2MDkwfQ.Ivk36K7629DVF_oSCeDqNO_N_DhDS8n37_mN09qmHXE";
        }

        Intent intent = getIntent();
        if (intent.hasExtra("type")) {
            TYPE = intent.getStringExtra("type");
        }
        if (intent.hasExtra("status")) {
            validated = intent.getStringExtra("status");
            driver_id = intent.getStringExtra("driver_id");
            Log.e("DriverID","getStringExtra " + driver_id);

        }

        if(validated == "validated"){

            Log.e("DriverPosition","SetVissibility Gone");

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getTransitWithDriveID = new GetTransitWithDriveID("5cc0cba41b30070017e13fc2");
                    getTransitWithDriveID.execute();
                }
            }, 0, 1000);

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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_location = findViewById(R.id.search_button_location);
        btn_destination = findViewById(R.id.search_button_destination);
        btn_rides = findViewById(R.id.search_button_ride);

        final EditText ed_destination = findViewById(R.id.editText_destination);
        final EditText ed_location = findViewById(R.id.editText_location);

        user = (User) AppDatabase.getInstance(this).userDao().getUser();

        btn_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation(ed_location);
                setDestinationLocation(ed_destination);
                ArrayList<MarkerOptions> markers = new ArrayList<>();

                mCurrentMarkerOptions = new MarkerOptions().position(new LatLng(mCurrentAddress.getLatitude(), mCurrentAddress.getLongitude()));
                mDestinationMarkerOptions = new MarkerOptions().position(new LatLng(mDestinationAddress.getLatitude(), mDestinationAddress.getLongitude()));
                markers.add(mCurrentMarkerOptions);
                markers.add(mDestinationMarkerOptions);
                //new FetchUrl(MapsActivity.this).execute(getUrl(mCurrentMarkerOptions.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");

                //show all marker on the map
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerOptions marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();

                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.animateCamera(cu);

                String currentLocationId = AppDatabase.getInstance(v.getContext()).userDao().getUserCurrentPositionId();
                String destinationId = AppDatabase.getInstance(v.getContext()).userDao().getDestinationId();

                putPositionTask = new PutPositionTask(currentLocationId, mCurrentMarkerOptions);
                putPositionTask.execute((Void) null);
                putPositionTask = new PutPositionTask(destinationId, mDestinationMarkerOptions);
                putPositionTask.execute((Void) null);

                // Then just use the following:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_destination != null) {
                    btn_destination.setEnabled(true);
                    setCurrentLocation(ed_location);
                }
            }
        });

        btn_rides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cree un nouveau transit
                CreateTransitTask createTransitTask = new CreateTransitTask(user.get_id(), user.getCurrent_position_id(), user.getDestination_id());
                createTransitTask.execute((Void) null);

                //if driver
                if (user.isDriver()) {
                    Handler handlerCheckPassager = new Handler();

                    int delayCheckPassager = 5000; //milliseconds
                    waypoints = null;
                    handlerCheckPassager.postDelayed(new Runnable() {
                        public void run() {
                            getTransitByDriverIdTask = new GetTransitByDriverIdTask(user.get_id());
                            getTransitByDriverIdTask.execute((Void) null);
                            handlerCheckPassager.removeCallbacks(this);

                            if(transit != null && transit.getPassager() != null){
                                for(Transit.Passager p : transit.getPassager()){
                                    if(p.getPassagerStatus().equalsIgnoreCase("waiting")){
                                        handlerCheckPassager.removeCallbacks(this);
                                        GetPassagerByIdTask getPassagerByIdTask = new GetPassagerByIdTask(p.getPassagerId());
                                        getPassagerByIdTask.execute((Void) null);

                                        break;
                                    }

                                }
                            }
                            Toast.makeText(v.getContext(), "Test find passager", Toast.LENGTH_LONG).show();
                            if(passagerTrouver){
                                handlerCheckPassager.removeCallbacks(this);
                            }else{
                                handlerCheckPassager.postDelayed(this, delayCheckPassager);
                            }




                        }
                    }, delayCheckPassager);



                    Handler handlerChangePosition = new Handler();
                    int delay = 5000; //milliseconds
                    handlerChangePosition.postDelayed(new Runnable() {
                        public void run() {
                            String currentLocationId = AppDatabase.getInstance(getApplicationContext()).userDao().getUserCurrentPositionId();
                            mCurrentMarkerOptions = new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                            putPositionTask = new PutPositionTask(currentLocationId, mCurrentMarkerOptions);
                            //drawRoute(waypoints,"driving", false, true);
                            //new FetchUrl(MapsActivity.this).execute(getUrl(newCurrentPosition.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");

                            putPositionTask.execute((Void) null);
                            handlerChangePosition.postDelayed(this, delay);
                        }
                    }, delay);
                } else {

                    Intent i = new Intent(MapsActivity.this, CardViewActivity.class);
                    startActivity(i);
                }
            }
        });
        if (!validated.equals("")) {
            btn_rides.setVisibility(View.GONE);
            btn_location.setVisibility(View.GONE);
            btn_destination.setVisibility(View.GONE);
            ed_destination.setVisibility(View.GONE);
            ed_location.setVisibility(View.GONE);

        }
        if (TYPE.equals("Driver")) {
            btn_rides.setText("Start ride");
        }
    }
    public class GetTransitWithDriveID extends AsyncTask<Void, Void, Boolean> {
        String idDriver;
        GetTransitWithDriveID(String id) {
            idDriver = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(MapsActivity.this);
            request.getTransitforPosition(idDriver);
            return true;
        }
    }

    @Override
    public void getTransitforPosition(Call<DriverInfoForTransit> call, Response<DriverInfoForTransit> response) {
        DriverInfoForTransit mess = response.body();
        getPositionCurrent_idPosition = new GetPositionCurrent_IDPosition(mess.getDriver_current_positionID());
        getPositionCurrent_idPosition.execute();

    }

    public class GetPositionCurrent_IDPosition extends AsyncTask<Void, Void, Boolean> {
        String iDPosition;

        GetPositionCurrent_IDPosition(String idPositionDrive) {
            iDPosition = idPositionDrive;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(MapsActivity.this);
            request.getPosition(token, iDPosition);
            //Log.e("DriverPosition","doInBackground to get GetPositionCurrent_IDPosition");
            return true;
        }
    }

    @Override
    public void getPosition(Call<Position> call, Response<Position> response) {
        Position mess = response.body();
//        Log.e("DriverPosition", " getPosition(Call<Position> call, Response<Position> response) ");
//        Log.e("DriverPosition", "Message : "+response.message() + "  Code : "+ response.code());
        Log.e("DriverPosition", "Latitude: " + mess.getLat());
        Log.e("DriverPosition", "Longitude : " + mess.getLng());
        Log.e("DriverPosition----", mess.getUserId());


        changeMarkerPosition(new LatLng(mess.getLat(), mess.getLng()));

    }

    public void changeMarkerPosition(LatLng latLng) {
        mDriverCurrentmarkerOptions.position(latLng);
        mDriverCurrentmarkerOptions.title("Current Driver Location Position");
        mDriverCurrentmarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fiat));
        if (mDriverCurrent == null) {
            mDriverCurrent = mMap.addMarker(mDriverCurrentmarkerOptions);
        }
        mDriverCurrent.setPosition(latLng);
    }

    private void setUpMarkers(View v) {
        ArrayList<MarkerOptions> markers = new ArrayList<>();

        mCurrentMarkerOptions = new MarkerOptions().position(new LatLng(mCurrentAddress.getLatitude(), mCurrentAddress.getLongitude()));
        mDestinationMarkerOptions = new MarkerOptions().position(new LatLng(mDestinationAddress.getLatitude(), mDestinationAddress.getLongitude()));
        markers.add(mCurrentMarkerOptions);
        markers.add(mDestinationMarkerOptions);
        //new FetchUrl(MapsActivity.this).execute(getUrl(mCurrentMarkerOptions.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");


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

    private void updatePosition(String id, MarkerOptions marker) {
        Position position = new Position(marker.getPosition().latitude, marker.getPosition().longitude);

        Call<Position> call = api.putPosition(token, id, position);

        call.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
            }
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

//        //Get driver position et put the marker on the map
//        moveDriver();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void setCurrentLocation(View view) {
        mCurrentAddress = onMapSearch((EditText) view);
    }

    public void setDestinationLocation(View view) {
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

    private void moveDriver(double lat, double lon) {
        if (mMap == null) {
            return;
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (validated == "validated"){
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Driver :")
                            .snippet("Estimated arrival time")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.voiture_icon))
                            .infoWindowAnchor(0.5f, 0.5f);
            }
                // Marker m = mMap.addMarker(markerOptions);
            }
        }, 0, 1000);
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
        } catch (SecurityException e) {
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
        } catch (SecurityException e) {
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

    public void confirm(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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

    private String getUrl(LatLng current, LatLng destination, String directionMode) {
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

    public class UpdateStatusToValidatedTask extends AsyncTask<Void, Void, Boolean> {

        private String passagerId;
        //        private final String mPosition;
        private String status;
        private String driverID;

        UpdateStatusToValidatedTask(String passagerId, String driverID) {
            this.passagerId = passagerId;
            this.status = "validated";
            this.driverID = driverID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StatusInfo statusInfo = new StatusInfo(status, passagerId, driverID);
            Call<Transit> call = api.updateStatus(statusInfo);
            call.enqueue(new Callback<Transit>() {
                @Override
                public void onResponse(Call<Transit> call, Response<Transit> response) {
                    getPassagerCurrentPositionTask = new GetPassagerCurrentPositionTask(passager.getUser_info().getCurrent_position_id());
                    getPassagerCurrentPositionTask.execute((Void) null);
                    getPassagerDestinationPositionTask = new GetPassagerDestinationPositionTask(passager.getUser_info().getDestination_id());
                    getPassagerDestinationPositionTask.execute((Void) null);
                }

                @Override
                public void onFailure(Call<Transit> call, Throwable t) {
                }
            });
            return null;
        }
    }

    public class UpdateStatusToRefusedTask extends AsyncTask<Void, Void, Boolean> {

        private String passagerId;
        //        private final String mPosition;
        private String status;
        private String driverID;

        UpdateStatusToRefusedTask(String passagerId, String driverID) {
            this.passagerId = passagerId;
            this.status = "refused";
            this.driverID = driverID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StatusInfo statusInfo = new StatusInfo(passagerId, status, driverID);
            Call<Transit> call = api.updateStatus(statusInfo);
            call.enqueue(new Callback<Transit>() {
                @Override
                public void onResponse(Call<Transit> call, Response<Transit> response) {

                }
                @Override
                public void onFailure(Call<Transit> call, Throwable t) {
                }
            });
            return null;
        }
    }



    public class CreateTransitTask extends AsyncTask<Void, Void, Boolean> {
        private String driverID;
        private String driver_current_positionID;
        private String driver_destination_positionID;

        public CreateTransitTask(String driverID, String driver_current_positionID, String driver_destination_positionID) {
            this.driverID = driverID;
            this.driver_current_positionID = driver_current_positionID;
            this.driver_destination_positionID = driver_destination_positionID;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            DriverInfoForTransit driverInfoForTransit = new DriverInfoForTransit(driverID, driver_current_positionID, driver_destination_positionID);
            Call<Transit> call = api.createTransit(driverInfoForTransit);
            call.enqueue(new Callback<Transit>() {
                @Override
                public void onResponse(Call<Transit> call, Response<Transit> response) {
                }
                @Override
                public void onFailure(Call<Transit> call, Throwable t) {
                }
            });
            return null;

        }
    }

    public class GetTransitByDriverIdTask extends AsyncTask<Void, Void, Boolean> {

        private String driverID;
        public GetTransitByDriverIdTask(String driverID) {
            this.driverID = driverID;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            Call<Transit> call = api.getTransit(driverID);
            call.enqueue(new Callback<Transit>() {
                @Override
                public void onResponse(Call<Transit> call, Response<Transit> response) {
                    transit = response.body();
                }

                @Override
                public void onFailure(Call<Transit> call, Throwable t) {
                }
            });
            return null;
        }
    }

    public class GetPassagerCurrentPositionTask extends AsyncTask<Void, Void, Boolean> {
        private String positionId;
        public GetPassagerCurrentPositionTask(String positionId) {
            this.positionId = positionId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Call<Position> call = api.getPosition(token, positionId);
            call.enqueue(new Callback<Position>() {
                @Override
                public void onResponse(Call<Position> call, Response<Position> response) {
                    Position position = response.body();
                    passagerCurrentMarkerOptions = new MarkerOptions().position(new LatLng(position.getLat(), position.getLng())).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mMap.addMarker(passagerCurrentMarkerOptions);

                }

                @Override
                public void onFailure(Call<Position> call, Throwable t) {
                }
            });
            return null;
        }
    }

    public class GetPassagerDestinationPositionTask extends AsyncTask<Void, Void, Boolean> {
        private String positionId;

        public GetPassagerDestinationPositionTask(String positionId) {
            this.positionId = positionId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Call<Position> call = api.getPosition(token, positionId);
            call.enqueue(new Callback<Position>() {
                @Override
                public void onResponse(Call<Position> call, Response<Position> response) {
                    Position position = response.body();
                    passagerDestinationMarkerOptions = new MarkerOptions().position(new LatLng(position.getLat(), position.getLng())).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mMap.addMarker(passagerDestinationMarkerOptions);

                    waypoints = new ArrayList<LatLng>();
                    waypoints.add(new LatLng(mCurrentMarkerOptions.getPosition().latitude, mCurrentMarkerOptions.getPosition().longitude));
                    waypoints.add(new LatLng(passagerCurrentMarkerOptions.getPosition().latitude, passagerCurrentMarkerOptions.getPosition().longitude));
                    waypoints.add(new LatLng(passagerDestinationMarkerOptions.getPosition().latitude, passagerDestinationMarkerOptions.getPosition().longitude));
                    waypoints.add(new LatLng(mDestinationMarkerOptions.getPosition().latitude, mDestinationMarkerOptions.getPosition().longitude));
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : waypoints) {
                        builder.include(latLng);
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.animateCamera(cu);

                    drawRoute(waypoints,"driving", false, true);
                }

                @Override
                public void onFailure(Call<Position> call, Throwable t) {
                }
            });
            return null;
        }

    }

    public class GetPassagerByIdTask extends AsyncTask<Void, Void, Boolean> {


        private String passagerId;


        public GetPassagerByIdTask(String passsagerId) {
            this.passagerId = passsagerId;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Call<UserInfo> call = api.getUserById(passagerId);
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    passager = response.body();
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    updateStatusToValidatedTask = new UpdateStatusToValidatedTask(passager.getUser_info().get_id(), user.get_id());
                                    updateStatusToValidatedTask.execute((Void) null);



                                    passagerTrouver = true;
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    updateStatusToRefusedTask = new UpdateStatusToRefusedTask(passager.getUser_info().get_id(), user.get_id());
                                    updateStatusToRefusedTask.execute((Void) null);
                                    passagerTrouver = false;
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Confirm");

                    builder.setMessage("Accepter vous le passager :" + passager.getUser_info().getName());
                    builder.setPositiveButton("Yes", dialogClickListener);
                    builder.setNegativeButton("No", dialogClickListener);
                    builder.show();
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                }
            });
            return null;
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
                        //new FetchUrl(MapsActivity.this).execute(getUrl(newCurrentPosition.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");
                        putPositionTask.execute((Void) null);
                        handler.postDelayed(test, 4000);
                    }
                };

                handler.postDelayed(test, 0);
            }

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

        }

        public class GetPositionCurrent_IDPosition extends AsyncTask<Void, Void, Boolean> {
            String iDPosition;

            GetPositionCurrent_IDPosition(String idPositionDrive) {
                iDPosition = idPositionDrive;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                User_http_request request = new User_http_request(MapsActivity.this);
                request.getPosition(token,iDPosition);
                //Log.e("DriverPosition","doInBackground to get GetPositionCurrent_IDPosition");
                return true;
            }
        }
    }
    public boolean drawRoute(ArrayList<LatLng> points, String mode, boolean withIndications, boolean optimize)
    {

        String url = makeURL(points,mode,optimize);
        new connectAsyncTask(url,withIndications).execute();
        return true;



    }

    private String makeURL (ArrayList<LatLng> points, String mode, boolean optimize){
        StringBuilder urlString = new StringBuilder();

        if(mode == null)
            mode = "driving";

        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append( points.get(0).latitude);
        urlString.append(',');
        urlString.append(points.get(0).longitude);
        urlString.append("&destination=");
        urlString.append(points.get(3).latitude);
        urlString.append(',');
        urlString.append(points.get(3).longitude);

        urlString.append("&waypoints=");
        if(optimize)
            urlString.append("optimize:true|");
        urlString.append( points.get(1).latitude);
        urlString.append(',');
        urlString.append(points.get(1).longitude);

        for(int i=2;i<points.size()-1;i++)
        {
            urlString.append('|');
            urlString.append( points.get(i).latitude);
            urlString.append(',');
            urlString.append(points.get(i).longitude);
        }


        urlString.append("&sensor=true&mode="+mode);
        urlString.append("&key=" + getString(R.string.google_maps_key));


        return urlString.toString();
    }



    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }




    private class connectAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        boolean steps;
        connectAsyncTask(String urlPass, boolean withSteps){
            url = urlPass;
            steps = withSteps;

        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result,steps);
            }
        }
    }

    private void drawPath(String  result, boolean withSteps) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(4)
                        .color(Color.BLUE).geodesic(true));
            }


            if(withSteps)
            {
                JSONArray arrayLegs = routes.getJSONArray("legs");
                JSONObject legs = arrayLegs.getJSONObject(0);
                JSONArray stepsArray = legs.getJSONArray("steps");
                //put initial point

                for(int i=0;i<stepsArray.length();i++)
                {
                    Step step = new Step(stepsArray.getJSONObject(i));
                    mMap.addMarker(new MarkerOptions()
                            .position(step.location)
                            .title(step.distance)
                            .snippet(step.instructions)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                }
            }

        }
        catch (JSONException e) {

        }
    }


    /**
     * Class that represent every step of the directions. It store distance, location and instructions
     */
    private class Step
    {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON)
        {
            JSONObject startLocation;
            try {

                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"),startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
