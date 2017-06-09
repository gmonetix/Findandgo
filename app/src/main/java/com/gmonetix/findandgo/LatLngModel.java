package com.gmonetix.findandgo;

/**
 * @author Gmonetix
 */

public class LatLngModel {

    private double latitude;
    private double longitude;
    private String number;

    public LatLngModel(double latitude, double longitude, String number) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setNumber(number);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
