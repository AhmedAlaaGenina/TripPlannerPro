package com.ahmedg.tripplannerpro.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "trip_table")

public class TripModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tripName;
    private String source;
    private String destination;
    private String time;
    private String direction;
    private String repetition;
    private ArrayList<String> notes;
    private Double lat;
    private Double longt;
    private long alarm_time;

    public TripModel(String tripName, String source, String destination, String time, String direction,
                     String repetition, ArrayList<String> notes, Double lat, Double longt, long alarm_time) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.direction = direction;
        this.repetition = repetition;
        this.notes = notes;
        this.lat = lat;
        this.longt = longt;
        this.alarm_time = alarm_time;
    }

    protected TripModel(Parcel in) {
        id = in.readInt();
        tripName = in.readString();
        source = in.readString();
        destination = in.readString();
        time = in.readString();
        direction = in.readString();
        repetition = in.readString();
        notes = in.createStringArrayList();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            longt = null;
        } else {
            longt = in.readDouble();
        }
        alarm_time = in.readLong();
    }

    public static final Creator<TripModel> CREATOR = new Creator<TripModel>() {
        @Override
        public TripModel createFromParcel(Parcel in) {
            return new TripModel(in);
        }

        @Override
        public TripModel[] newArray(int size) {
            return new TripModel[size];
        }
    };

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

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
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

    public long getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(long alarm_time) {
        this.alarm_time = alarm_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tripName);
        dest.writeString(source);
        dest.writeString(destination);
        dest.writeString(time);
        dest.writeString(direction);
        dest.writeString(repetition);
        dest.writeStringList(notes);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (longt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longt);
        }
        dest.writeLong(alarm_time);
    }
}