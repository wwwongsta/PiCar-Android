package com.example.picar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.picar.R;

public class RideStatusActivity extends AppCompatActivity {
    TextView rideStatus;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status);
        rideStatus = findViewById(R.id.Status);
        progressBar = findViewById(R.id.progressBar);
    }
}
