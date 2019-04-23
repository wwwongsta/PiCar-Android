package com.example.picar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picar.R;

public class RideStatusActivity extends AppCompatActivity {
    TextView rideStatus;
    ProgressBar progressBar;
    String STATUS = "Waiting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status);
        rideStatus = findViewById(R.id.Status);
        progressBar = findViewById(R.id.progressBar);

        if(STATUS.equals("Waiting")){
            //waiting response
            rideStatus.setText(STATUS+"...");
        }else if(STATUS.equals("Validated")){
            Toast.makeText(this, STATUS, Toast.LENGTH_LONG).show();
            rideStatus.setText("Retreiving Driver Info");
            //go back to maps and see driver pulling up
        }else if(STATUS.equals("Refused")){
            Toast.makeText(this, STATUS, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
