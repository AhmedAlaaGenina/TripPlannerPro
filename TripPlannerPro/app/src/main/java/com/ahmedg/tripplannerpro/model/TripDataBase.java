package com.ahmedg.tripplannerpro.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TripModel.class, TripModelHistory.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class TripDataBase extends RoomDatabase {

    private static TripDataBase instance;

    public abstract TripDao tripDao();
    public abstract TripDaoHistory tripDaoHistory();

    public static synchronized TripDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TripDataBase.class, "trip_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
