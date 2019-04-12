package com.example.picar.retrofit;

public class Position {

    /**
     * {
     *   "_id": "5cb0bbc3dfd8360017f28389",
     *   "lat": 1,
     *   "lng": 48,
     *   "userId": "5cb0bbc3dfd8360017f28388",
     *   "__v": 0
     * }
     */
    private String _id;
    private double lat;
    private double lng;
    private String userId;

    public String get_id() {
        return _id;
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
}
