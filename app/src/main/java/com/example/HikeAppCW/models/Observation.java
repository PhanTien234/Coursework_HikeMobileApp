package com.example.HikeAppCW.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "observation")
public class Observation {
    @PrimaryKey(autoGenerate = true)
    public long observationId;

    public String observationName;
    public String observationTime;
    public String observationDate;
    public String observationWeather;
    public String observationComment;

    public long obHikeId;

    public Observation(long observationId, String observationName, String observationTime, String observationDate, String observationWeather,
                       String observationComment, long obHikeId) {
        this.observationId = observationId;
        this.observationName = observationName;
        this.observationTime = observationTime;
        this.observationDate = observationDate;
        this.observationWeather = observationWeather;
        this.observationComment = observationComment;
        this.obHikeId = obHikeId;
    }

    public Observation(long id){
        this.obHikeId = id;
    }

    public Observation(){}

    public long getObservationId() {
        return observationId;
    }

    public void setObservationId(long observationId) {
        this.observationId = observationId;
    }

    public String getObservationName() {
        return observationName;
    }

    public void setObservationName(String observationName) {
        this.observationName = observationName;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public String getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(String observationDate) {
        this.observationDate = observationDate;
    }

    public String getObservationWeather() {
        return observationWeather;
    }

    public void setObservationWeather(String observationWeather) {
        this.observationWeather = observationWeather;
    }

    public String getObservationComment() {
        return observationComment;
    }

    public void setObservationComment(String observationComment) {
        this.observationComment = observationComment;
    }

    public long getObHikeId() {
        return obHikeId;
    }

    public void setObHikeId(long obHikeId) {
        this.obHikeId = obHikeId;
    }
}
