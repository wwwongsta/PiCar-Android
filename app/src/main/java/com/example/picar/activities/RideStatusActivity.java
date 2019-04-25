package com.example.picar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.picar.R;
import com.example.picar.database.entity.Transit;
import com.example.picar.retrofit.PiCarApi;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideStatusActivity extends AppCompatActivity {
    TextView rideStatus;
    ProgressBar progressBar;
    //change when we have passengers
    String STATUS = "validated";
    private PiCarApi api;
    String driver_id;
    String driver_location_id;
    String driver_destination_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status);
        rideStatus = findViewById(R.id.Status);
        progressBar = findViewById(R.id.progressBar);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PiCarApi.class);
        Call<Transit> call = api.getTransit("5cc0cba41b30070017e13fc2");

        call.enqueue(new Callback<Transit>() {
            @Override
            public void onResponse(Call<Transit> call, Response<Transit> response) {
                Transit transits = response.body();
                List<Transit.Passager> passager = transits.getPassager();
                driver_id = transits.getDriverID();
                driver_location_id = transits.getDriver_current_positionID();
                driver_destination_id = transits.getDriver_destination_positionID();
                for(Transit.Passager p : passager){
                    if(p.getPassagerId().equals("5cc0cba41b30070017e13fc2")){
                        STATUS += p.getPassagerStatus();
                    }
                }
            }

            @Override
            public void onFailure(Call<Transit> call, Throwable t) {
                rideStatus.setText(t.getMessage());
                return;
            }
        });
        Timer my_timer = new Timer("my_timer");
        my_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
        if(!STATUS.equals("validated") || !STATUS.equals("refused")){
            //waiting response
            rideStatus.setText("Waiting...");
        }
        if(STATUS.equals("validated")){
            rideStatus.setText("Retreiving Driver Info");
            Intent passager = new Intent(RideStatusActivity.this, MapsActivity.class);
            passager.putExtra("type", "Passager");
            passager.putExtra("status", "validated");
            passager.putExtra("driver_id", driver_id);
            passager.putExtra("driver_current_location_id", driver_location_id);
            passager.putExtra("driver_destination_id", driver_destination_id);

            startActivity(passager);
            finish();
            my_timer.cancel();
        }
        if(STATUS.equals("refused")){
            my_timer.cancel();
            finish();
        }
            }
        }, 0, 1000);
    }
}
