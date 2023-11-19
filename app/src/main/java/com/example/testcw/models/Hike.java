package com.example.testcw.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "hikes")
public class Hike {
    @PrimaryKey (autoGenerate = true)
    public long hike_id;
    public String name;
    public String location;
    public String date;
    public String parking;
    public String length;
    public String level;
    public String description;

    public Hike(long hike_id, String name, String location, String date,
                String parking, String length, String level, String description) {
        this.hike_id = hike_id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.level = level;
        this.description = description;
    }
    public Hike(){}
}
