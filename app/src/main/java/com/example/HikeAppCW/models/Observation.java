package com.example.HikeAppCW.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "observation")
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

}
