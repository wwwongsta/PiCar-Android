package com.example.picar.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.picar.Preferences;
import com.example.picar.R;
import com.example.picar.database.entity.Position;
import com.example.picar.retrofit.http_request.User_http_request;
import com.example.picar.retrofit.model.DriverInfoForTransit;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

public class SelectedDriver extends AppCompatActivity implements OnMapReadyCallback, User_http_request.UserHttpError, User_http_request.UserHttpResponse {

    /*Marker*/
    private MarkerOptions mDriverCurrentmarkerOptions = new MarkerOptions();
    ;
    private Marker mDriverCurrent;

    /*AsynTask*/
    private GetTransitWithDriveID getTransitWithDriveID = null;
    private GetPositionCurrent_IDPosition getPositionCurrent_idPosition = null;

    /*GoogleMap*/
    private GoogleMap mMap;

    /*Variable*/
    String token = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedmap);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        token = Preferences.getInstance(getBaseContext()).getString("Authorization");
        if (!token.equalsIgnoreCase("")) {
            token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWNhYmE2YmUwOWJhYjkyN2QxMWIwMTRhIiwiaWF0IjoxNTU1MzM2MDkwfQ.Ivk36K7629DVF_oSCeDqNO_N_DhDS8n37_mN09qmHXE";
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getTransitWithDriveID = new GetTransitWithDriveID("5cc0cba41b30070017e13fc2");
                getTransitWithDriveID.execute();
            }
        }, 0, 1000);
//        String idDriver;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if (extras == null) {
//                idDriver = null;
//            } else {
//                idDriver = extras.getString("driver_id");
//                if (idDriver != null) {
//                    Log.e("DriverPosition", idDriver);
//                    new Timer().scheduleAtFixedRate(new TimerTask() {
//                        @Override
//                        public void run() {
//                            getTransitWithDriveID = new GetTransitWithDriveID("5cc0cba41b30070017e13fc2");
//                            getTransitWithDriveID.execute();
//                        }
//                    }, 0, 1000);
//                }
//            }
//        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public class GetTransitWithDriveID extends AsyncTask<Void, Void, Boolean> {
        String idDriver;

        GetTransitWithDriveID(String id) {
            idDriver = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(SelectedDriver.this);
            request.getTransitforPosition(idDriver);
            //   Log.e("DriverPosition","doInBackground to get driver current position id");


            return true;
        }
    }

    @Override
    public void getTransitforPosition(Call<DriverInfoForTransit> call, Response<DriverInfoForTransit> response) {
        DriverInfoForTransit mess = response.body();
//        Log.e("DriverPosition", " Response<DriverInfoForTransit> response ");
//        Log.e("DriverPosition", response.message());
//        Log.e("DriverPosition - current id", mess.getDriver_current_positionID());
//        Log.e("DriverPosition - current destiation", mess.getDriver_destination_positionID());
//        Log.e("DriverPosition- current driver id", mess.getDriverID());

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
            User_http_request request = new User_http_request(SelectedDriver.this);
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
}
