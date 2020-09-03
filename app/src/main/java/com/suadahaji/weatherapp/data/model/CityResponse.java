package com.suadahaji.weatherapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CityResponse {
    @SerializedName("id")
    public long id;
    @SerializedName("dt")
    public long dt;
    @SerializedName("name")
    public String name;
}
