package com.example.picar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picar.R;
import com.example.picar.database.entity.Transit;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.model.StatusUpdateResponse;

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
    String STATUS = "";
    private PiCarApi api;

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
        Call<Transit> call = api.getTransit("5cb108645a33a30017af6e7d");

        call.enqueue(new Callback<Transit>() {
            @Override
            public void onResponse(Call<Transit> call, Response<Transit> response) {
                Transit transits = response.body();
                List<Transit.Passager> passager = transits.getPassager();

                for(Transit.Passager p : passager){
                    if(p.getPassagerId().equals("5cbf6de5294550001707e016")){
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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
        if(!STATUS.equals("validated") || !STATUS.equals("refused")){
            //waiting response
            rideStatus.setText("Waiting...");
        }
        if(STATUS.equals("validated")){
            rideStatus.setText("Retreiving Driver Info");
            //go back to maps and see driver pulling up
        }
        if(STATUS.equals("refused")){
            finish();
        }
            }
        }, 0, 1000);
        Toast.makeText(RideStatusActivity.this, STATUS, Toast.LENGTH_LONG).show();
    }
}
