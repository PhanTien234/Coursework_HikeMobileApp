package com.example.HikeAppCW.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.HikeAppCW.dao.HikeDao;
import com.example.HikeAppCW.dao.ObservationDao;
import com.example.HikeAppCW.models.Hike;
import com.example.HikeAppCW.models.Observation;

@Database(entities = {Hike.class, Observation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HikeDao hikeDao();
    public abstract ObservationDao observationDao();
}
