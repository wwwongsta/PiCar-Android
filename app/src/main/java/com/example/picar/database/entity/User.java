package com.example.picar.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "users")
public class User {

    public User() {
    }


    @Ignore
    public User(int id, int payement_id, String name, String family_name,
                String cell, String email, String password, String user_photo, String current_position_id, String destination_id) {
        this.id = id;
        this.current_position_id=current_position_id;
        this.destination_id=destination_id;
        this.payement_id = payement_id;
        this.family_name = family_name;
        this.name = name;
        this.cell = cell;
        this.email = email;
        this.password = password;
        this.user_photo = user_photo;
    }
//
//    @Override
//    public String toString() {
//        return
//                "id= " + id + " position = " + position + " payement_id= " + payement_id + " name= " + name + " family_name= " + family_name + " cell= " + cell + " email= " + email + " password= " + password + " user_photo= " + user_photo;
//    }


    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private int id;

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

    @ColumnInfo(name = "cell")
    private String cell;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "user_photo")
    private String user_photo;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
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

    public void setCurrent_position_id(String current_position_id) {
        this.current_position_id = current_position_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }
}
