package com.example.picar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.picar.Preferences;
import com.example.picar.R;
import com.example.picar.database.AppDatabase;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.model.user_type.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoActivity  extends AppCompatActivity {
    String email, name, family_name, password, phone;
    TextView t_email, t_name, t_first_name, t_password, t_phone;
    private PiCarApi api;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        t_email = findViewById(R.id.email);
        t_name = findViewById(R.id.name);
        t_first_name = findViewById(R.id.firstname);
        t_password = findViewById(R.id.password);
        t_phone = findViewById(R.id.phone);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PiCarApi.class);

        token = Preferences.getInstance(getBaseContext()).getString("Authorization");
        if (!token.equalsIgnoreCase("")) {
            token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWNhYmE2YmUwOWJhYjkyN2QxMWIwMTRhIiwiaWF0IjoxNTU1MzM2MDkwfQ.Ivk36K7629DVF_oSCeDqNO_N_DhDS8n37_mN09qmHXE";
        }

        //Call<UserInfo> call = api.getUserByEmail(token,);

        List<User> user_list = AppDatabase.getInstance(this).userDao().getListUser();
            for(User user : user_list){
                t_email.setText(email);
                t_name.setText(user.getName());
                t_first_name.setText(user.getFamily_name());
                t_password.setText(user.getPassword());
                t_phone.setText(user.getPhone());
            }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
