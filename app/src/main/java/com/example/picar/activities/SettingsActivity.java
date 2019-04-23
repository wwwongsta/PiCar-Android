package com.example.picar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.picar.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_AUTHORIZATION = "authorization_key";
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
/*Example comment Ecrire dans la clé d'authorization
/* public void authorization(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_PREF_AUTHORIZATION, "as");
        editor.apply();

    }*/
