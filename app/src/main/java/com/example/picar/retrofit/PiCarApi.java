package com.example.picar.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface PiCarApi {

    @GET("user/all")
    Call<List<User>> getAllUser();

    @GET("user")
    Call<User> getUser(@Header("email") String email);
}
