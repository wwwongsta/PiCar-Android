package com.example.picar.retrofit.model;

public class PositionDestination {
    private String idPositionActuelle;
    private String idDestination;

    public PositionDestination(String idPositionActuelle, String idDestination) {
        this.idPositionActuelle = idPositionActuelle;
        this.idDestination = idDestination;
    }

    public String getIdPositionActuelle() {
        return idPositionActuelle;
    }

    public String getIdDestination() {
        return idDestination;
    }
}
