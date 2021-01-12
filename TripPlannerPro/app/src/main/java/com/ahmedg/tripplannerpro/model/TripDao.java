package com.ahmedg.tripplannerpro.model;


import androidx.room.Dao;
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
    Single<List<TripModel>> getTrips();
}
