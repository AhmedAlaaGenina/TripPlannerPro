package com.ahmedg.tripplannerpro.model;

public class TripModel {
    private String tripName;
    private String source;
    private String destination;
    private boolean status;
    private String date;
    private String time;

    public TripModel(String tripName, String source, String destination, boolean status, String date, String time) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.date = date;
        this.time = time;
    }


    public TripModel(String tripName, String source, String destination, boolean status) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
