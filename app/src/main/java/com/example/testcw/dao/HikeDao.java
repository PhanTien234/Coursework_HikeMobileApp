package com.example.testcw.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testcw.models.Hike;

import java.util.List;

@Dao
public interface HikeDao {
    @Insert
    long insertHike(Hike hike);

    @Query("SELECT * FROM hikes ORDER BY name")
    List<Hike> getAllHike();

    @Delete
    void deleteHike(Hike hike);

    @Query("DELETE FROM hikes")
    void deleteAll();

    @Update
    void updateHike(Hike hike);

    @Query("SELECT * FROM hikes WHERE name LIKE :name ")
    List<Hike> searchHikeName(String name);

}
