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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.data.model.CityResponse;
import com.suadahaji.weatherapp.data.database.DbContract;
import com.suadahaji.weatherapp.data.database.DbHelper;

import java.net.URL;

import static com.suadahaji.weatherapp.utils.NetworkUtils.buildUrlFromLatLng;
import static com.suadahaji.weatherapp.utils.NetworkUtils.getResponseFromHttpUrl;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng city = new LatLng(39.3999, -8.2245);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                city).zoom(6).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.setOnMapClickListener(this);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
        new WeatherTask().execute(buildUrlFromLatLng(latLng));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public class WeatherTask extends AsyncTask<URL, Void, CityResponse> {
        @Override
        protected CityResponse doInBackground(URL... urls) {
            URL url = urls[0];

            CityResponse weatherResponse = null;
            try {
                String results = getResponseFromHttpUrl(url);
                Gson gson = new GsonBuilder().create();
                weatherResponse = gson.fromJson(results, CityResponse.class);
                bookmarkCity(weatherResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return weatherResponse;
        }
    }

    private void bookmarkCity(CityResponse weatherResponse) {
        DbHelper dbHelper = new DbHelper(getActivity());
        DbContract dbContract = new DbContract(dbHelper);
        if (!weatherResponse.name.isEmpty())
            dbContract.bookmarkCity(weatherResponse);
    }
}

