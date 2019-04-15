package com.example.picar.retrofit.model.user_type;

import com.example.picar.database.entity.User;

public class UserInfo {
    private User user_info;
    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public User getUser_info() {
        return user_info;
    }

}
