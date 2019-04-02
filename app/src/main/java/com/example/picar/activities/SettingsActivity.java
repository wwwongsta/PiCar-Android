package com.example.picar.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.picar.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_STAY_LOGGED_IN = "preference_stay_logged_in";
    public static final String KEY_PREF_STAY_SERVER_ADDRESS = "preference_server_address";
    public static final String KEY_PREF_STAY_WEB_APP = "preference_web_app";
    public static final String KEY_PREF_STAY_WEB_APP_PORT = "preference_web_app_port";
    public static final String KEY_PREF_SYNCHRO_PORT = "preference_synchro_port";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
