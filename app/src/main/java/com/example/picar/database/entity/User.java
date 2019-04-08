package com.example.picar.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "users")
public class User {

    public User() {}



    @Ignore
    public User(int id, int payement_id, String name, String family_name, int cell, String email, String password, String user_photo) {
        this.id = id;
        this.payement_id = payement_id;
        this.family_name = family_name;
        this.name = name;
        this.cell = cell;
        this.email = email;
        this.password = password;
        this.user_photo = user_photo;
    }

    @Override
    public String toString() {
        return
                "id= " + id + " payement_id= " + payement_id + " name= " + name  + " family_name= " + family_name  + " cell= " + cell + " email= " + email + " password= " + password + " user_photo= " + user_photo ;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private int id;

    @ColumnInfo(name = "payement_id")
    private int payement_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "family_name")
    private String family_name;

    @ColumnInfo(name = "cell")
    private int cell;

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

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
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


}
