package com.suadahaji.weatherapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {
    @SerializedName("id")
    public long id;
    @SerializedName("dt")
    public long dt;
    @SerializedName("name")
    public String name;
    @SerializedName("main")
    public MainTemperature main;
    @SerializedName("weather")
    public List<Weather> weather;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;

    class Weather{
        @SerializedName("id")
        public long id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }
    class MainTemperature {
        @SerializedName("temp_min")
        public float temp_min;
        @SerializedName("temp_max")
        public float temp_max;
        @SerializedName("pressure")
        public int pressure;
        @SerializedName("humidity")
        public int humidity;
    }

    class Wind {
        @SerializedName("speed")
        public float speed;
    }

    class Clouds {
        @SerializedName("all")
        public int all;
    }
}
