package com.example.picar.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.picar.R;
import com.example.picar.retrofit.http_request.User_http_request;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.model.position_type.Position;
import com.example.picar.retrofit.model.user_type.User;
import com.example.picar.retrofit.model.user_type.UserInfo;
import com.example.picar.retrofit.model.user_type.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity implements
        User_http_request.UserHttpResponse,
        User_http_request.UserHttpError {
    TextView tx;
    private PiCarApi api;
    private User_http_request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        tx = (TextView) findViewById(R.id.retrofitTest);
        request = new User_http_request(this);
    }
}
