package com.example.picar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static Preferences yourPreference;
    private static SharedPreferences sharedPreferences;

    public static Preferences getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new Preferences(context);
        }
        return yourPreference;
    }

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    Preferences(Context context) {

        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference",Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
    public void putString(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }
    public void putBoolean(String key,Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean getBoolean(String check) {
        boolean myValue = sharedPreferences.getBoolean(check,false);
        return myValue;
    }
}