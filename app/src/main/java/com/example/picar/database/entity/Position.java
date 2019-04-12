package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "positions")
public class Position {
    @PrimaryKey
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "latitude")
    int latitude;
    @ColumnInfo(name = "longitude")
    int longitude;
    @ColumnInfo(name = "userId")
    int userId;

    public Position() {
    }
@Ignore
    public Position(int id, int latitude, int longitude, int userId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
