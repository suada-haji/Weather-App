package com.suadahaji.weatherapp.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.data.model.CityResponse;
import com.suadahaji.weatherapp.utils.Constants;

import java.net.URL;

import static com.suadahaji.weatherapp.utils.NetworkUtils.buildUrlFromCityId;
import static com.suadahaji.weatherapp.utils.NetworkUtils.getResponseFromHttpUrl;

public class CityDetailsFragment extends Fragment {
    private String cityName;

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
            cityName = getArguments().getString(Constants.COLUMN_CITY_NAME);
            new ForecastTask().execute(buildUrlFromCityId(id));
        }
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
            super.onPostExecute(cityResponse);
        }
    }
}
