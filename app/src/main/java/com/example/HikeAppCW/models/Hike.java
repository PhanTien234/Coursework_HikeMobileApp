package com.example.HikeAppCW.models;

public class Hike {
    private long hike_id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private String length;
    private String level;
    private String description;

    // Constructors

    public Hike() {
    }

    public Hike(String name, String location, String date, String parking, String length, String level, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.level = level;
        this.description = description;
    }

    // Getters and Setters

    public long getId() {
        return hike_id;
    }

    public void setId(long id) {
        this.hike_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}