package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "transits")
public class Transit {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;


    @ColumnInfo(name = "driverID")
    private String driverId;

    @ColumnInfo(name = "driver_current_positionID")
    private String driver_current_position_id;

    @ColumnInfo(name = "driver_destination_positionID")
    private String driver_destination_position_id;

    @Ignore
    @ColumnInfo(name = "passager")
    private List<Passager> passager;

    public Transit() {
    }

    public Transit(@NonNull String id, String driverId, String driver_current_position_id, String driver_destination_position_id, List<Passager> passager) {
        this.id = id;
        this.driverId = driverId;
        this.driver_current_position_id = driver_current_position_id;
        this.driver_destination_position_id = driver_destination_position_id;
        this.passager = passager;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriver_current_position_id() {
        return driver_current_position_id;
    }

    public void setDriver_current_position_id(String driver_current_position_id) {
        this.driver_current_position_id = driver_current_position_id;
    }

    public String getDriver_destination_position_id() {
        return driver_destination_position_id;
    }

    public void setDriver_destination_position_id(String driver_destination_position_id) {
        this.driver_destination_position_id = driver_destination_position_id;
    }

    public List<Passager> getPassager() {
        return passager;
    }

    public void setPassager(ArrayList<Passager> passagers) {
        this.passager = passagers;
    }

    public class Passager {

        private String id;
        private String passagerId;
        private String passagerStatus;

        public Passager(String id, String passagerId, String passagerStatus) {
            this.passagerId = passagerId;
            this.passagerStatus = passagerStatus;
        }

        public Passager() {
        }

        public String getPassagerId() {
            return passagerId;
        }

        public void setPassagerId(String passagerId) {
            this.passagerId = passagerId;
        }

        public String getPassagerStatus() {
            return passagerStatus;
        }

        public void setPassagerStatus(String passagerStatus) {
            this.passagerStatus = passagerStatus;
        }
    }
}
