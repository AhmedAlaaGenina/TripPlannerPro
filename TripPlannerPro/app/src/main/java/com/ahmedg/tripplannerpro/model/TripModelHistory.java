package com.ahmedg.tripplannerpro.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
@Entity(tableName = "trip_table_history")
public class TripModelHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tripName;
    private String source;
    private String destination;
    private boolean status;
    private String time;
    private String direction;
    private String repetition;
    private Double lat;
    private Double longt;
    private Double lato;
    private Double longto;

    public TripModelHistory(String tripName, String source, String destination, boolean status, String time,
                            String direction, String repetition, Double lat, Double longt, Double lato, Double longto) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.time = time;
        this.direction = direction;
        this.repetition = repetition;
        this.lat = lat;
        this.longt = longt;
        this.lato = lato;
        this.longto = longto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongt() {
        return longt;
    }

    public void setLongt(Double longt) {
        this.longt = longt;
    }

    public Double getLato() {
        return lato;
    }

    public void setLato(Double lato) {
        this.lato = lato;
    }

    public Double getLongto() {
        return longto;
    }

    public void setLongto(Double longto) {
        this.longto = longto;
    }
}