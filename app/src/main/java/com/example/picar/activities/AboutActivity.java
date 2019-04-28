package com.example.picar.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.picar.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);


    }
    public void intentVersDoc(View v ){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ihatam.docs.apiary.io/?fbclid=IwAR3VWTkfOIhMR4VYJJhM9cNtSoHTOjnYZZVpLnKtU8obE5tRamEs7ONIJco#reference/position/position-collection/creer-un-utilisateur"));
        startActivity(browserIntent);

    }
}
