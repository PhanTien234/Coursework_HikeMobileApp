package com.example.testcw.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.testcw.dao.HikeDao;
import com.example.testcw.dao.ObservationDao;
import com.example.testcw.models.Hike;
import com.example.testcw.models.Observation;

@Database(entities = {Hike.class, Observation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HikeDao hikeDao();
    public abstract ObservationDao observationDao();
}
