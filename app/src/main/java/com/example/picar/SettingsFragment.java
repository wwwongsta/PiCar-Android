package com.example.picar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.picar.activities.MainActivity;
import com.example.picar.activities.SettingsActivity;
import com.example.picar.activities.UserInfoActivity;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences sharedPreferences;
    String email;
    SharedPreferences SP;
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);


        SP = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor prefsEditor = SP.edit();
        prefsEditor.putBoolean(SettingsActivity.KEY_PREF_STAY_LOGGED_IN,false);
        prefsEditor.apply();

        Preference myPref = (Preference) findPreference(SettingsActivity.KEY_PREF_STAY_LOGGED_IN);
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Preferences.getInstance(getContext()).putBoolean(SettingsActivity.KEY_PREF_STAY_LOGGED_IN,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Do you want to log out ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        startActivity(new Intent(getContext(), MainActivity.class));
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        Preference user_info = findPreference("user_info");
        user_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SP = PreferenceManager.getDefaultSharedPreferences(getContext());
                email = SP.getString("Email", "");

                Intent i = new Intent(getContext(), UserInfoActivity.class);
                i.putExtra("email", email);
                startActivity(i);
                return false;
            }
        });
    }


}