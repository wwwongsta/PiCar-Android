package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "positions")
public class Position {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    String _id;
    @ColumnInfo(name = "lat")
    double lat;
    @ColumnInfo(name = "lng")
    double lng;
    @ColumnInfo(name = "userId")
    String userId;

    public Position() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Ignore
    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
