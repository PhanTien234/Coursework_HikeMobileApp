package com.example.testcw.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testcw.models.Observation;

import java.util.List;

@Dao
public interface ObservationDao {

    @Insert
    long insertObservation(Observation observation);

    @Query("SELECT * FROM observation WHERE ob_hike_id = :id")
    List<Observation> getAllObservation(long id);

    @Query("SELECT * FROM observation")
    List<Observation> getAllOb();
    @Delete
    void deleteObservation(Observation observation);

    @Update
    void updateObservation(Observation observation);

}
