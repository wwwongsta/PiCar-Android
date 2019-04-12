package com.example.picar.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.picar.R;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.Position;
import com.example.picar.retrofit.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    TextView tx;
    private PiCarApi api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        tx = (TextView) findViewById(R.id.retrofitTest);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PiCarApi.class);
//        getusers();
        getAllPosition();
    }
    private void getAllPosition(){
        Call<List<Position>> call = api.getAllPosition();
        call.enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                if(!response.isSuccessful()){
                    tx.setText("code : "+ response.code());
                    return;
                }
                List<Position> positions = response.body();
                for (Position position:positions){
                    String content = "";
                    content += "Lat :" + position.getLat() + "\n";
                    content += "Lng :"+ position.getLng()+"\n";
                    tx.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                tx.setText(t.getMessage());
            }
        });
    }
    private void getusers(){
        Call<List<User>> call = api.getAllUser();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    tx.setText("code : "+ response.code());
                    return;
                }
                List<User> users = response.body();
                for (User user:users){
                    String content = "";
                    content += "Email :" + user.getEmail() + "\n";
                    content += "Password :"+ user.getPassword()+"\n";
                    tx.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                tx.setText(t.getMessage());
            }
        });
    }
}
