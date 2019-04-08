package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "transits")
public class Transit {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "driver_id")
    private int driver_id;

    @ColumnInfo(name = "path")
    private String path;

    public Transit() {
    }
    @Ignore
    public Transit(int driver_id, String path) {
        this.driver_id = driver_id;
        this.path = path;
    }


    @Override
    public String toString() {
        return "driver_id= " + driver_id + " path= " + path;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public String getPath() {
        return path;
    }
}
