package com.hairhub.BookAnAppointment;

public class DistanceResult {
    private String salonName;
    private String salonAddress;
    private Location location;
    private double distance;

    public DistanceResult(String salonName, String salonAddress, Location location, double distance) {
        this.salonName = salonName;
        this.salonAddress = salonAddress;
        this.location = location;
        this.distance = distance;
    }

    public String getSalonName() {
        return salonName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public Location getLocation() {
        return location;
    }

    public double getDistance() {
        return distance;
    }
}

