package com.example.picar.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PiCarApi {

    @GET("user/all")
    Call<List<User>> getAllUser();

    @GET("user/")
    Call<List<User>> getUserByEmail ();

    @GET("position/")
    Call<List<Position>> getAllPosition();
}
