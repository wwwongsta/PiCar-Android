package com.example.picar.activities;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.picar.Preferences;
import com.example.picar.R;
import com.example.picar.database.AppDatabase;
import com.example.picar.database.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLogIn();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton driverButton = (ImageButton) findViewById(R.id.button_driver);
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getInstance(v.getContext()).userDao().updateIsDriver(true);
                Intent driver = new Intent(MainActivity.this, MapsActivity.class);
                driver.putExtra("type", "Driver");
                startActivity(driver);
            }
        });

        ImageButton passengerButton = (ImageButton) findViewById(R.id.button_passenger);
        passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getInstance(v.getContext()).userDao().updateIsDriver(false);
                Intent passager = new Intent(MainActivity.this, MapsActivity.class);
                passager.putExtra("type", "Passager");
                startActivity(passager);
            }
        });

        Address address;
        try {
            address = getCoordinatesOfAddress(this, "Montreal");
            setTitle(address.getLatitude() + ", " + address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class GetUserInfo extends AsyncTask<Void, Void, User> {

        private final AppDatabase db;
        private final TextView txt;

        public GetUserInfo(Context c, TextView t) {
            this.db = AppDatabase.getInstance(c);
            this.txt = t;
        }

        @Override
        protected User doInBackground(Void... params) {
            User user = null;
            List<User> users = db.userDao().getListUser();
            if (users.size() > 0)
                user = users.get(0);
            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            if (user != null)
                txt.setText(user.toString());
        }

        @Override
        protected void onCancelled() {
        }
    }

    public Address getCoordinatesOfAddress(Context context, String myLocation) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(myLocation, 1);
        Address address = addresses.get(0);
        return address;
    }


    public boolean checkLogIn() {
        boolean stayLoggedIn = Preferences.getInstance(getBaseContext()).getBoolean(SettingsActivity.KEY_PREF_STAY_LOGGED_IN);
        if (stayLoggedIn == false) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return stayLoggedIn;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_help) {
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
            return true;
        } else if (id == R.id.nav_your_trips) {

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
