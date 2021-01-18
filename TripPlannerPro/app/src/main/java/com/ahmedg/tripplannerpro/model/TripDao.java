package com.ahmedg.tripplannerpro.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface TripDao {
    @Insert
    Completable insertTrip(TripModel tripModel);

    @Query("select * from trip_table")
    Single<List<TripModel>> getAllTrips();

    @Query("SELECT * FROM trip_table WHERE id =:id")
    Single<TripModel> getTripById(int id);

    @Delete
    Completable deleteTrip(TripModel tripModel);

    @Query("UPDATE trip_table set tripName= :tripName ,source =:source,destination=:destination ,date=:date," +
            "time=:time,direction=:direction,repetition=:repetition WHERE id=:id")
    Completable update(int id, String tripName, String source, String destination,
                       String date, String time, String direction, String repetition);

    @Query("select notes from trip_table WHERE id=:id")
    Single<List<String>> getListNotes(int id);

    @Query("UPDATE trip_table SET notes =:notes WHERE id=:id")
    Completable updateNotes(int id, List<String> notes);

}
