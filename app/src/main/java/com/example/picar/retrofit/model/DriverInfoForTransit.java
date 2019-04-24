package com.example.picar.retrofit.model;

public class DriverInfoForTransit {
    private String driverID;
    private String driver_current_positionID;
    private String driver_destination_positionID;

    public DriverInfoForTransit(String driverID, String driver_current_positionID, String driver_destination_positionID) {
        this.driverID = driverID;
        this.driver_current_positionID = driver_current_positionID;
        this.driver_destination_positionID = driver_destination_positionID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
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
}
