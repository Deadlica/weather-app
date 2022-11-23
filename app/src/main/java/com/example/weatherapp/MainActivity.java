package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Coordinates 62.394344, 17.284073

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView temperatureText, windSpeedText, cloudinessText, precipitationText;
    private EditText latitudeEditText, longitudeEditText;
    private Button refreshButton;
    private String baseURL = "https://api.met.no/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureText = findViewById(R.id.temperatureText);
        windSpeedText = findViewById(R.id.windSpeedText);
        cloudinessText = findViewById(R.id.cloudinessText);
        precipitationText = findViewById(R.id.percipitationText);
        latitudeEditText = findViewById(R.id.latitudeInput);
        longitudeEditText = findViewById(R.id.longitudeInput);
        refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        if(latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, "Please enter a location by specifying the latitude and longitude. Ex. 62.17, 17.33", Toast.LENGTH_LONG).show();
            return;
        }

        if(latitude.startsWith(".")) {
            latitude = "0" + latitude;
        }

        if(longitude.startsWith(".")) {
            longitude = "0" + longitude;
        }

        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);
        refreshWeatherData(lat, lon);
    }

    private void refreshWeatherData(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<WeatherData> call = weatherAPI.getData("compact.json", latitude, longitude);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "uh oh :c", Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherData data = response.body();

                try {
                    Double airTemperature = data.getProperties().timeseries.get(0).data.instant.details.air_temperature;
                    temperatureText.setText("Temperature: " + airTemperature.toString() + " celsius");
                } catch (NullPointerException e) {
                    temperatureText.setText("Temperature: N/A");
                }

                try {
                    Double windSpeed = data.getProperties().timeseries.get(0).data.instant.details.wind_speed;
                    windSpeedText.setText("WindSpeed: " + windSpeed.toString() + " mps");
                } catch (NullPointerException e) {
                    windSpeedText.setText("WindSpeed: N/A");
                }

                try {
                    Double cloudiness = data.getProperties().timeseries.get(0).data.instant.details.cloud_area_fraction;
                    cloudinessText.setText("Cloudiness: " + cloudiness.toString() + "%");
                } catch (NullPointerException e) {
                    cloudinessText.setText("Cloudiness: N/A");
                }

                try {
                    Double precipitationMin = data.getProperties().timeseries.get(0).data.next12Hours.details.precipitation_amount_min;
                    precipitationText.setText("Precipitation: Between " + precipitationMin.toString() + " mm");
                } catch (NullPointerException e) {
                    precipitationText.setText("Precipitation: Between N/A mm");
                }

                try {
                    Double precipitationMax = data.getProperties().timeseries.get(0).data.next12Hours.details.precipitation_amount_max;
                    precipitationText.append(" and " + precipitationMax.toString() + " mm");
                } catch (NullPointerException e) {
                    precipitationText.append(" and N/A mm");
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}