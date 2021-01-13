package com.ahmedg.tripplannerpro.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface TripDao {
    @Insert
    Completable insertTrip(TripModel tripModel);


    @Query("select * from trip_table")
    Single<List<TripModel>> getTrips();

    @Delete
    Completable deleteTrip(TripModel tripModel);

    @Query("UPDATE trip_table set tripName= :tripName ,source =:source,destination=:destination ,status=:status,date=:date," +
            "time=:time,direction=:direction,repetition=:repetition,notes=:notes WHERE id=:id")
    Completable update(int id,String tripName, String source, String destination, boolean status,
                       String date, String time, String direction, String repetition, ArrayList<String> notes);

    @Query("select notes from trip_table WHERE id")
    Single<List<String>>getListNotes();
}
