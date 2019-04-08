package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "users_car",foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "user_id",
        childColumns = "user_id"),
        indices = {@Index("user_id")})
public class UserCar {

    public UserCar() {
    }

    @Ignore
    public UserCar(int id, int user_id, String picture_car, String couleur,
                   String brand, String registration, String categorie) {
        this.id = id;
        this.user_id = user_id;
        this.picture_car = picture_car;
        this.couleur = couleur;
        this.brand = brand;
        this.registration = registration;
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "couleur= " + couleur + " brand= " + brand + " registration= " + registration + " categorie= " + categorie ;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "picture_car")
    private String picture_car;

    @ColumnInfo(name = "couleur")
    private String couleur;

    @ColumnInfo(name = "brand")
    private String brand;

    @ColumnInfo(name = "registration")
    private String registration;


    @ColumnInfo(name = "categorie")
    private String categorie;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPicture_car() {
        return picture_car;
    }

    public void setPicture_car(String picture_car) {
        this.picture_car = picture_car;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
