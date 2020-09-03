package com.suadahaji.weatherapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suadahaji.weatherapp.utils.Constants;

public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase writeDatabase;

    public DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        writeDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME + " (" +
                Constants.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                Constants.COLUMN_WEATHER_DATE + "  TEXT,  " +
                Constants.COLUMN_DATE_BOOKMARKED + "  DEFAULT CURRENT_TIMESTAMP,  " +
                Constants.COLUMN_CITY_NAME + "  TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    public SQLiteDatabase getDB() {
        return writeDatabase;
    }
}
