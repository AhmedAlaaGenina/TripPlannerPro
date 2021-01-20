package com.ahmedg.tripplannerpro.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface TripDaoHistory {
    @Insert
    Completable insertTripHistory(TripModelHistory tripModelHistory);

    @Delete
    Completable deleteTripHistory(TripModelHistory tripModelHistory);

    @Query("select * from trip_table_history")
    Single<List<TripModelHistory>> getAllTrips();

    @Query("SELECT * FROM trip_table_history WHERE id =:id")
    Single<TripModelHistory> getTripHistoryById(int id);

}
