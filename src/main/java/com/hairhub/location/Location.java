package com.hairhub.location;
public class Location {
    private String zipcode;
    private double latitude;
    private double longitude;

    // Constructor
    public Location(String zipcode, double latitude, double longitude) {
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter methods
    public String getZipcode() {
        return zipcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
