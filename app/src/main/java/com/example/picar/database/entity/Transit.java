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
    private String driverID;

    @ColumnInfo(name = "driver_current_positionID")
    private String driver_current_positionID;

    @ColumnInfo(name = "driver_destination_positionID")
    private String driver_destination_positionID;

    @Ignore
    @ColumnInfo(name = "passager")
    private List<Passager> passager;


    public Transit() {
    }

    public Transit(@NonNull String id, String driverId, String driver_current_position_id, String driver_destination_positionID, List<Passager> passager) {
        this.id = id;
        this.driverID = driverId;
        this.driver_current_positionID = driver_current_position_id;
        this.driver_destination_positionID = driver_destination_positionID;
        this.passager = passager;
    }



    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }


    public String getDriver_current_positionID() {
        return driver_current_positionID;
    }

    public void setDriver_current_positionID(String driver_current_positionID) {
        this.driver_current_positionID = driver_current_positionID;
    }

    public String getDriver_destination_positionID() {
        return driver_destination_positionID;
    }

    public void setDriver_destination_positionID(String driver_destination_positionID) {
        this.driver_destination_positionID = driver_destination_positionID;
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
