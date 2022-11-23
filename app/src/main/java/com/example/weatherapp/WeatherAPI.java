package com.example.weatherapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @Headers("User-Agent: Samuel Greenberg")
    @GET("weatherapi/locationforecast/2.0/{format}")
    Call<WeatherData> getData(
            @Path("format") String format,
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );
}
