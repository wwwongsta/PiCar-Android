package com.example.picar.retrofit;

public class UserLogin {
    private String email;
    private String password;
    private boolean token;

    public UserLogin(String email,boolean token) {
        this.email = email;
        this.token = token;
    }

    public UserLogin(String email,boolean token, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isToken() {
        return token;
    }
}
