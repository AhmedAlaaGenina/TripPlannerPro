package com.ahmedg.tripplannerpro.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import io.reactivex.Completable;

@Dao
public interface TripDaoHistory {
    @Insert
    Completable insertTripHistory(TripModelHistory tripModelHistory);

    @Delete
    Completable deleteTripHistory(TripModelHistory tripModelHistory);

}
