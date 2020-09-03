package com.suadahaji.weatherapp.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.suadahaji.weatherapp.data.CityResponse;
import com.suadahaji.weatherapp.utils.Constants;

import java.util.ArrayList;

public class DbContract {
    private DbHelper dbHelper;

    private String[] columns = {
            Constants.COLUMN_ID,
            Constants.COLUMN_WEATHER_DATE,
            Constants.COLUMN_DATE_BOOKMARKED,
            Constants.COLUMN_CITY_NAME
    };

    public DbContract(DbHelper weatherDbHelper) {
        this.dbHelper = weatherDbHelper;
    }

    public void bookmarkCity(CityResponse cityResponse) throws SQLException {
        if (!isCityBookmarked(cityResponse)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.COLUMN_ID, cityResponse.id);
            contentValues.put(Constants.COLUMN_WEATHER_DATE, cityResponse.dt);
            contentValues.put(Constants.COLUMN_DATE_BOOKMARKED, System.currentTimeMillis());
            contentValues.put(Constants.COLUMN_CITY_NAME, cityResponse.name);
            dbHelper.getDB().insert(Constants.TABLE_NAME, null, contentValues);
        } else {
            updateCityBookmark(cityResponse);
        }
    }

    private void updateCityBookmark(CityResponse cityResponse) {
        String dbQuery = "UPDATE " + Constants.TABLE_NAME + " SET " +
                Constants.COLUMN_WEATHER_DATE + " = " + cityResponse.dt +
                Constants.COLUMN_DATE_BOOKMARKED + " = " + System.currentTimeMillis() + " WHERE " +
                Constants.COLUMN_CITY_NAME + " = '" + cityResponse.name + "' AND " +
                Constants.COLUMN_WEATHER_DATE + " = '" + cityResponse.dt + "'";
        dbHelper.getDB().execSQL(dbQuery);
    }

    private boolean isCityBookmarked(CityResponse cityResponse) {
        String dbQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.COLUMN_CITY_NAME + " = '" + cityResponse.name + "' AND " +
                Constants.COLUMN_WEATHER_DATE + " = '" + cityResponse.dt + "'";
        Cursor cursor = dbHelper.getDB().rawQuery(dbQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }
    public void deleteBookmarkedCity(CityResponse cityResponse) {
        String dbQuery = "DELETE FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.COLUMN_CITY_NAME + " = '" + cityResponse.name + "'";
        dbHelper.getDB().execSQL(dbQuery);
    }

    public ArrayList<CityResponse> getAllCities() {
        Cursor cursor = dbHelper.getDB().query(Constants.TABLE_NAME,
                columns, null, null, null, null, Constants.COLUMN_DATE_BOOKMARKED + " DESC");
        ArrayList<CityResponse> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            cities.add(getFromCursor(cursor));
        }
        cursor.close();
        return cities;
    }

    private CityResponse getFromCursor(Cursor cursor) {
        CityResponse city = new CityResponse();

        city.id = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.COLUMN_ID));
        city.dt = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.COLUMN_WEATHER_DATE));
        city.name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CITY_NAME));
        return city;
    }
}
