package com.ahmedg.tripplannerpro.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trip_table")
public class TripModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tripName;
    private String source;
    private String destination;
    private boolean status;
    private String date;
    private String time;
    private String direction;
    private String repetition;
    private ArrayList<String> notes;

    public TripModel( String tripName, String source, String destination, boolean status,
                     String date, String time, String direction, String repetition, ArrayList<String> notes) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.date = date;
        this.time = time;
        this.direction = direction;
        this.repetition = repetition;
        this.notes = notes;
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

    public  ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}