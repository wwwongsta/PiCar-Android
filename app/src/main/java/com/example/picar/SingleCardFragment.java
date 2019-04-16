package com.example.picar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.picar.activities.CardViewActivity;
import com.example.picar.activities.MainActivity;
import com.example.picar.retrofit.PiCarApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public abstract class SingleCardFragment extends AppCompatActivity {
    Context context;

    private GoogleMap mMap;

    private PiCarApi api;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view_activity);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmet_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmet_container, fragment).commit();
        }
    }
}
