package com.example.picar.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "users")
public class User {

    public User() {
    }
    @Ignore
    public User(String email, String password, String family_name, String name, String phone, boolean isDriver) {
        this.email = email;
        this.password = password;
        this.family_name = family_name;
        this.name = name;
        this.phone = phone;
        this.isDriver = isDriver;
    }
    
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    @ColumnInfo(name = "current_position_id")
    private String current_position_id;

    @ColumnInfo(name = "destination_id")
    private String destination_id;

    @ColumnInfo(name = "payement_id")
    private int payement_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "family_name")
    private String family_name;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "user_photo")
    private String user_photo;

    @ColumnInfo(name = "isDriver")
    private boolean isDriver;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getPayement_id() {
        return payement_id;
    }

    public void setPayement_id(int payement_id) {
        this.payement_id = payement_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getCurrent_position_id() {
        return current_position_id;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public void setCurrent_position_id(String current_position_id) {
        this.current_position_id = current_position_id;

    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    @Override
    public String toString() {
        return "User{\\n" +
                "_id='" + _id + '\'' +
                ",\n current_position_id='" + current_position_id + '\'' +
                ",\n destination_id='" + destination_id + '\'' +
                ",\n payement_id=" + payement_id +
                ",\n name='" + name + '\'' +
                ",\n family_name='" + family_name + '\'' +
                ",\n phone='" + phone + '\'' +
                ",\n email='" + email + '\'' +
                ",\n password='" + password + '\'' +
                ",\n user_photo='" + user_photo + '\'' +
                "\n}";
    }
}
