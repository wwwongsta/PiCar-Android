package com.example.picar.retrofit.model;

public class StatusInfo {
    /***
     *             "status":"validated",
     *             "passagerId":"5cbf6de5294550001707e016"
     */
    private String status;
    private String passagerId;
    private String driverID;
    
    public StatusInfo(String status, String passagerId,String driverID) {
        this.status = status;
        this.passagerId = passagerId;
        this.driverID = driverID;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getStatus() {
        return status;
    }

    public String getPassagerId() {
        return passagerId;
    }
}
