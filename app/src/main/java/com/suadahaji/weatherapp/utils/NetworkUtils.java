package com.suadahaji.weatherapp.utils;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.suadahaji.weatherapp.BuildConfig;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final String units = "metric";
    private static final int numDays = 5;
    private static final String LAT_PARAM = "lat";
    private static final String LON_PARAM = "lon";
    private static final String UNITS_PARAM = "units";
    private static final String DAYS_PARAM = "cnt";
    private static final String APPID_PARAM = "appid";

    public static URL buildUrl(LatLng latLng) {
        Uri weatherUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, String.valueOf(latLng.latitude))
                .appendQueryParameter(LON_PARAM, String.valueOf(latLng.longitude))
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                .build();
        URL weatherUrl = null;
        try {
            weatherUrl = new URL(weatherUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return weatherUrl;
    }

    public static String getResponseFromHttpUrl(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
