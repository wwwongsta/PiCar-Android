package com.example.picar.retrofit.model;

public class StatusInfo {
    /***
     *             "status":"validated",
     *             "passagerId":"5cbf6de5294550001707e016"
     */
    private String status;
    private String passagerId;

    public StatusInfo(String status, String passagerId) {
        this.status = status;
        this.passagerId = passagerId;
    }

    public String getStatus() {
        return status;
    }

    public String getPassagerId() {
        return passagerId;
    }
}
