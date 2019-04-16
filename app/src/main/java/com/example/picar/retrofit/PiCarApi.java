package com.example.picar.retrofit;

import com.example.picar.database.entity.Position;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.database.entity.User;
import com.example.picar.retrofit.model.user_type.UserInfo;
import com.example.picar.retrofit.model.user_type.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PiCarApi {

    @GET("user/all")
    Call<List<User>> getAllUser(); // debug route

    @POST("user/create")
    Call<Message> createUser(@Body User user);

    @POST("user/emailCheck")
    Call<UserInfo> checkEmail(@Body UserLogin user);

    @POST("user")
    Call<UserInfo> getUserByEmail(@Header("authorization") String token,@Body UserLogin user);

    @POST("user/login")
    Call<UserInfo> login(@Body UserLogin user);

    @GET("position/")
    Call<List<Position>> getAllPosition(); //debug route

    @PUT("position/{id}")
    Call<Position> putPosition(@Header("authorization") String token,@Path("id") String id, @Body Position position);

    @PUT("position/{id}")
    Call<Position> putPosition(@Path("id") String id, @Body Position position);

    @PUT("position/{id}")
    Call<Position> getPosition(@Header("authorization") String token,@Path("id") String id, @Body Position position);

}
