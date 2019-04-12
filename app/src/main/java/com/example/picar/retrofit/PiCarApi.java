package com.example.picar.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PiCarApi {

    @GET("user/all")
    Call<List<User>> getAllUser();


    @GET("user")
    Call<User> getUser(@Header("email") String email);

    @GET("position/")
    Call<List<Position>> getAllPosition();

    @PUT("position/{id}")
    Call<Position> putPosition(@Path("id") String id, @Body Position position);

}
