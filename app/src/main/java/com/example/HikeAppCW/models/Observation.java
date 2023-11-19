package com.example.HikeAppCW.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "observation")
public class Observation {
    @PrimaryKey(autoGenerate = true)
    public long observation_id;

    public String observation_name;
    public String observation_time;
    public String observation_date;
    public String observation_weather;
    public String observation_comment;

    public long ob_hike_id;

    public Observation(long observation_id, String observation_name, String observation_time, String observation_date , String observation_weather,
                       String observation_comment, long ob_hike_id) {
        this.observation_id = observation_id;
        this.observation_name = observation_name;
        this.observation_time = observation_time;
        this.observation_date = observation_date;
        this.observation_weather = observation_weather;
        this.observation_comment = observation_comment;
        this.ob_hike_id = ob_hike_id;
    }

    public Observation(long id){
        this.ob_hike_id = id;
    }

    public Observation(){}

    public long getObservation_id() {
        return observation_id;
    }

    public void setObservation_id(long observation_id) {
        this.observation_id = observation_id;
    }

    public String getObservation_name() {
        return observation_name;
    }

    public void setObservation_name(String observation_name) {
        this.observation_name = observation_name;
    }

    public String getObservation_time() {
        return observation_time;
    }

    public void setObservation_time(String observation_time) {
        this.observation_time = observation_time;
    }

    public String getObservation_date() {
        return observation_date;
    }

    public void setObservation_date(String observation_date) {
        this.observation_date = observation_date;
    }

    public String getObservation_weather() {
        return observation_weather;
    }

    public void setObservation_weather(String observation_weather) {
        this.observation_weather = observation_weather;
    }

    public String getObservation_comment() {
        return observation_comment;
    }

    public void setObservation_comment(String observation_comment) {
        this.observation_comment = observation_comment;
    }

    public long getOb_hike_id() {
        return ob_hike_id;
    }

    public void setOb_hike_id(long ob_hike_id) {
        this.ob_hike_id = ob_hike_id;
    }
}
