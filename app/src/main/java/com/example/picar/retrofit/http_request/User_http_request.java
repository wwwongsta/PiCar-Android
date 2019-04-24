package com.example.picar.retrofit.http_request;

import android.app.Activity;
import android.util.Log;

import com.example.picar.database.entity.Position;
import com.example.picar.database.entity.Transit;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.model.DriverInfoForTransit;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.database.entity.User;
import com.example.picar.retrofit.model.type_message.MessageUserEmailApproval;
import com.example.picar.retrofit.model.user_type.UserEmail;
import com.example.picar.retrofit.model.user_type.UserInfo;
import com.example.picar.retrofit.model.user_type.UserLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_http_request {
    public static String TAG = "User_http_request";
    private PiCarApi api;
    private UserHttpError errorHandler;
    private UserHttpResponse responseHandler;
    public User_http_request(Activity activity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.api = retrofit.create(PiCarApi.class);
        try{
            errorHandler = (UserHttpError) activity;
            responseHandler = (UserHttpResponse) activity;
        }catch (ClassCastException e){
            Log.e(TAG," On attach class cast exception " + e.getMessage());
        }
    }
    public interface UserHttpError{
        default void errorMessage(Call<Message> call, Throwable t){

        }
        default void errorEmail(Call<MessageUserEmailApproval> call, Throwable t){

        }

        default void errorGetByEmail(Call<UserInfo> call, Throwable t){

        }

        default void errorLogin(Call<UserInfo> call, Throwable t){

        }


        default void errorGetPosition(Call<Position> call, Throwable t){

        }
        default void getTransitforPosition(Call<DriverInfoForTransit> call, Throwable t){

        }

    }
    public interface UserHttpResponse{
        default void createUser(Call<Message> call, Response<Message> response){

        }
        default void checkEmail(Call<MessageUserEmailApproval> call, Response<MessageUserEmailApproval> response){

        }

        default void getByEmail(Call<UserInfo> call, Response<UserInfo> response){

        }

        default void login(Call<UserInfo> call, Response<UserInfo> response){

        }

        default  void getPosition(Call<Position> call, Response<Position> response){

        }

        default void getTransitforPosition(Call<DriverInfoForTransit> call, Response<DriverInfoForTransit> response){

        }
    }

    public void getTransitforPosition(String idDriver){

        Call<DriverInfoForTransit> call = api.getTransitforPosition(idDriver);
        call.enqueue(new Callback<DriverInfoForTransit>() {
            @Override
            public void onResponse(Call<DriverInfoForTransit> call, Response<DriverInfoForTransit> response) {
                responseHandler.getTransitforPosition(call,response);

            }
            @Override
            public void onFailure(Call<DriverInfoForTransit> call, Throwable t) {
                errorHandler.getTransitforPosition(call,t);
            }
        });

    }
    public void createUser(User user){
        Call<Message> call = api.createUser(user);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                responseHandler.createUser(call,response);
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                errorHandler.errorMessage(call,t);
            }
        });

    }
    public void checkEmail(UserEmail email){
        Call<MessageUserEmailApproval> call = api.checkEmail(email);
        call.enqueue(new Callback<MessageUserEmailApproval>() {
            @Override
            public void onResponse(Call<MessageUserEmailApproval> call, Response<MessageUserEmailApproval> response) {
                responseHandler.checkEmail(call,response);
            }
            @Override
            public void onFailure(Call<MessageUserEmailApproval> call, Throwable t) {
                errorHandler.errorEmail(call,t);
            }
        });
    }
    public void getUserByEmail(String token,UserLogin user){
        Call<UserInfo> call = api.getUserByEmail(token,user);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                responseHandler.getByEmail(call,response);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                errorHandler.errorGetByEmail(call,t);
            }
        });
    }
    public void login(UserLogin user){
        Call<UserInfo> call = api.login(user);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                responseHandler.login(call,response);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                errorHandler.errorLogin(call,t);
            }
        });
    }

    public void PutPosition(String token, String id, Position position){
        Call<Position> call = api.putPosition(token, id, position);
        call.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                responseHandler.getPosition(call, response);
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
                errorHandler.errorGetPosition(call, t);
            }

        });
    }



}
