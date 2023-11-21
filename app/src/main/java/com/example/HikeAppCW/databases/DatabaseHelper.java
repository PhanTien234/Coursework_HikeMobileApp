package com.example.HikeAppCW.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.HikeAppCW.models.Hike;
import com.example.HikeAppCW.models.Observation;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hikeApp_database";
    private static final int DATABASE_VERSION = 3;

    // Hike table
    private static final String TABLE_HIKE = "hikes";
    private static final String HIKE_ID = "hike_id";
    private static final String HIKE_NAME = "name";
    private static final String HIKE_LOCATION = "location";
    private static final String HIKE_DATE = "date";
    private static final String HIKE_PARKING = "parking";
    private static final String HIKE_LENGTH = "length";
    private static final String HIKE_LEVEL = "level";
    private static final String HIKE_DESCRIPTION = "description";

    // Observation table
    private static final String TABLE_OBSERVATION = "observation";
    private static final String OBSERVATION_ID = "observation_id";
    private static final String OBSERVATION_NAME = "observation_name";
    private static final String OBSERVATION_TIME = "observation_time";
    private static final String OBSERVATION_DATE = "observation_date";
    private static final String OBSERVATION_WEATHER = "observation_weather";
    private static final String OBSERVATION_COMMENT = "observation_comment";
    private static final String OB_HIKE_ID = "ob_hike_id";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HIKE = "CREATE TABLE " + TABLE_HIKE + "(" +
                HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HIKE_NAME + " TEXT, " +
                HIKE_LOCATION + " TEXT, " +
                HIKE_DATE + " TEXT, " +
                HIKE_PARKING + " TEXT, " +
                HIKE_LENGTH + " TEXT, " +
                HIKE_LEVEL + " TEXT, " +
                HIKE_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_TABLE_HIKE);

        String CREATE_TABLE_OBSERVATION = "CREATE TABLE " + TABLE_OBSERVATION + "(" +
                OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OBSERVATION_NAME + " TEXT, " +
                OBSERVATION_TIME + " TEXT, " +
                OBSERVATION_DATE + " TEXT, " +
                OBSERVATION_WEATHER + " TEXT, " +
                OBSERVATION_COMMENT + " TEXT, " +
                OB_HIKE_ID + " INTEGER, " +
                "FOREIGN KEY (" + OB_HIKE_ID + ") REFERENCES " + TABLE_HIKE + "(" + HIKE_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_TABLE_OBSERVATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKE + TABLE_OBSERVATION);
        onCreate(db);
    }


    // Insert a new hike
    public long insertHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HIKE_NAME, hike.getName());
        values.put(HIKE_LOCATION, hike.getLocation());
        values.put(HIKE_DATE, hike.getDate());
        values.put(HIKE_PARKING, hike.getParking());
        values.put(HIKE_LENGTH, hike.getLength());
        values.put(HIKE_LEVEL, hike.getLevel());
        values.put(HIKE_DESCRIPTION, hike.getDescription());

        // Insert row
        long hikeId = db.insert(TABLE_HIKE, null, values);
        return hikeId;
    }

    // Get all hikes

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to the list
        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getLong(cursor.getColumnIndex(HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndex(HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndex(HIKE_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndex(HIKE_DATE)));
                hike.setParking(cursor.getString(cursor.getColumnIndex(HIKE_PARKING)));
                hike.setLength(cursor.getString(cursor.getColumnIndex(HIKE_LENGTH)));
                hike.setLevel(cursor.getString(cursor.getColumnIndex(HIKE_LEVEL)));
                hike.setDescription(cursor.getString(cursor.getColumnIndex(HIKE_DESCRIPTION)));

                // Adding hike to list
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return hikeList;
    }

    // Delete a specific hike by ID
    public void deleteHike(long hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKE, HIKE_ID + " = ?", new String[]{String.valueOf(hikeId)});
    }

    // Delete all hikes
    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKE, null, null);
    }


    public List<Hike> searchHikeName(String name) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKE + " WHERE " + HIKE_NAME + " LIKE ?",
                new String[]{name});

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getLong(cursor.getColumnIndex(HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndex(HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndex(HIKE_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndex(HIKE_DATE)));
                hike.setParking(cursor.getString(cursor.getColumnIndex(HIKE_PARKING)));
                hike.setLength(cursor.getString(cursor.getColumnIndex(HIKE_LENGTH)));
                hike.setLevel(cursor.getString(cursor.getColumnIndex(HIKE_LEVEL)));
                hike.setDescription(cursor.getString(cursor.getColumnIndex(HIKE_DESCRIPTION)));

                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return hikeList;
    }

    // Update an existing hike
    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HIKE_NAME, hike.getName());
        values.put(HIKE_LOCATION, hike.getLocation());
        values.put(HIKE_DATE, hike.getDate());
        values.put(HIKE_PARKING, hike.getParking());
        values.put(HIKE_LENGTH, hike.getLength());
        values.put(HIKE_LEVEL, hike.getLevel());
        values.put(HIKE_DESCRIPTION, hike.getDescription());

        // Updating row
        return db.update(TABLE_HIKE, values, HIKE_ID + " = ?",
                new String[]{String.valueOf(hike.getId())});
    }

    // Insert a new observation
    public long insertObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBSERVATION_NAME, observation.getObservation_name());
        values.put(OBSERVATION_TIME, observation.getObservation_time());
        values.put(OBSERVATION_DATE, observation.getObservation_date());
        values.put(OBSERVATION_WEATHER, observation.getObservation_weather());
        values.put(OBSERVATION_COMMENT, observation.getObservation_comment());
        values.put(OB_HIKE_ID, observation.getOb_hike_id());

        // Insert row
        long observationId = db.insert(TABLE_OBSERVATION, null, values);
        return observationId;
    }

    // Get all observations for a specific hike

    public List<Observation> getObservationsForHike(long hikeId) {
        List<Observation> observationList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OBSERVATION +
                        " WHERE " + OB_HIKE_ID + " = ?",
                new String[]{String.valueOf(hikeId)});

        if (cursor.moveToFirst()) {
            do {
                Observation observation = new Observation();
                observation.observation_id = cursor.getLong(cursor.getColumnIndex(OBSERVATION_ID));
                observation.observation_name = cursor.getString(cursor.getColumnIndex(OBSERVATION_NAME));
                observation.observation_time = cursor.getString(cursor.getColumnIndex(OBSERVATION_TIME));
                observation.observation_date = cursor.getString(cursor.getColumnIndex(OBSERVATION_DATE));
                observation.observation_weather = cursor.getString(cursor.getColumnIndex(OBSERVATION_WEATHER));
                observation.observation_comment = cursor.getString(cursor.getColumnIndex(OBSERVATION_COMMENT));
                observation.ob_hike_id = cursor.getLong(cursor.getColumnIndex(OB_HIKE_ID));

                observationList.add(observation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return observationList;
    }

    // Update an existing observation
    public int updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBSERVATION_NAME, observation.getObservation_name());
        values.put(OBSERVATION_TIME, observation.getObservation_time());
        values.put(OBSERVATION_DATE, observation.getObservation_date());
        values.put(OBSERVATION_WEATHER, observation.getObservation_weather());
        values.put(OBSERVATION_COMMENT, observation.getObservation_comment());
        values.put(OB_HIKE_ID, observation.getOb_hike_id());

        // Updating row
        return db.update(TABLE_OBSERVATION, values, OBSERVATION_ID + " = ?",
                new String[]{String.valueOf(observation.getObservation_id())});
    }

    public void deleteObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATION, OBSERVATION_ID + " = ?",
                new String[]{String.valueOf(observation.observation_id)});
    }

    public void deleteAllObservations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATION, null, null);
    }
}
