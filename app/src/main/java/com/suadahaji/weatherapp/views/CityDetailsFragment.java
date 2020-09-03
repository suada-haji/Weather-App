package com.suadahaji.weatherapp.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.data.model.CityResponse;
import com.suadahaji.weatherapp.utils.Constants;
import com.suadahaji.weatherapp.utils.WeatherUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.suadahaji.weatherapp.utils.NetworkUtils.buildUrlFromCityId;
import static com.suadahaji.weatherapp.utils.NetworkUtils.getResponseFromHttpUrl;

public class CityDetailsFragment extends Fragment {
    private TextView cityName, weatherTime, weatherDesc, maxTemp, minTemp,
            humidity, cloudCoverage, windSpeed;
    private ImageView weatherIcon;
    private ProgressBar cityProgressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            long id = getArguments().getLong(Constants.COLUMN_CITY_ID);
            String cityName = getArguments().getString(Constants.COLUMN_CITY_NAME);
            new ForecastTask().execute(buildUrlFromCityId(id));
        }
        cityName = view.findViewById(R.id.cityName);
        weatherTime = view.findViewById(R.id.weatherTime);
        weatherDesc = view.findViewById(R.id.weatherDesc);
        maxTemp = view.findViewById(R.id.maxTemp);
        minTemp = view.findViewById(R.id.minTemp);
        humidity = view.findViewById(R.id.humidity);
        cloudCoverage = view.findViewById(R.id.cloudCoverage);
        windSpeed = view.findViewById(R.id.windSpeed);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        cityProgressBar = view.findViewById(R.id.cityProgressBar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public class ForecastTask extends AsyncTask<URL, Void, CityResponse> {
        @Override
        protected CityResponse doInBackground(URL... urls) {
            URL url = urls[0];

            CityResponse cityResponse = null;
            try {
                String results = getResponseFromHttpUrl(url);
                Gson gson = new GsonBuilder().create();
                cityResponse = gson.fromJson(results, CityResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cityResponse;
        }

        @Override
        protected void onPostExecute(CityResponse cityResponse) {
            cityProgressBar.setVisibility(View.GONE);
            super.onPostExecute(cityResponse);
            cityName.setText(cityResponse.name);

            weatherTime.setText(new SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(System.currentTimeMillis()));
            weatherDesc.setText(cityResponse.weather.get(0).description);
            maxTemp.setText(String.format(getString(R.string.temp_format), cityResponse.main.temp_max));
            minTemp.setText(String.format(getString(R.string.temp_format), cityResponse.main.temp_min));
            windSpeed.setText(String.format(getString(R.string.wind_format), cityResponse.wind.speed));

            humidity.setText(String.format(getString(R.string.humidity_format),cityResponse.main.humidity));
            cloudCoverage.setText(String.format(getString(R.string.cloud_coverage_format),cityResponse.clouds.all));
            weatherIcon.setImageResource(WeatherUtils.getIconForWeatherCondition(cityResponse.weather.get(0).icon));

        }
    }
}
