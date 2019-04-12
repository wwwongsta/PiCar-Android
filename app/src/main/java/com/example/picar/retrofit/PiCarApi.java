package com.example.picar.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PiCarApi {

    @GET("user/all")
    Call<List<User>> getAllUser();
}
