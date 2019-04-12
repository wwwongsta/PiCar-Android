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
    private String position;
    private String _id;

    @SerializedName("body")
    private String text;

    public String getFamily_name() {
        return family_name;
    }

    public String getName() {
        return name;
    }

    public Number getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
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

    public String getText() {
        return text;
    }
}
