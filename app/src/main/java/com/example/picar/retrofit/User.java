package com.example.picar.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    /**
     *     "email":"secondTest@mail.com",
     *     "password":unaTest
     */
    private String email;
    private String password;
    private String family_name;
    private String name;
    private Number phone;
    private String current_position_id;
    private String destination_id;
    private String _id;
    private String token;
    public User(String email, String password, String family_name, String name, Number phone) {
        this.email = email;
        this.password = password;
        this.family_name = family_name;
        this.name = name;
        this.phone = phone;
    }

    public String getCurrent_position_id() {
        return current_position_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getName() {
        return name;
    }

    public Number getPhone() {
        return phone;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
