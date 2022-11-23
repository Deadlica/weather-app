package com.example.weatherapp;

import com.example.weatherapp.json.Geometry;
import com.example.weatherapp.json.Properties;

import java.util.ArrayList;

public class WeatherData {
    private String type;
    private Geometry geometry;
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public String getType() {
        return type;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}