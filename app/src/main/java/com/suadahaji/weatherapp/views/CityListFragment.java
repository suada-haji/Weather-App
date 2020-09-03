package com.suadahaji.weatherapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.adapters.CityListAdapter;
import com.suadahaji.weatherapp.data.CityResponse;
import com.suadahaji.weatherapp.data.database.DbContract;
import com.suadahaji.weatherapp.data.database.DbHelper;

import java.util.List;

public class CityListFragment extends Fragment implements CityListAdapter.ItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView cityRecyclerView = view.findViewById(R.id.cityListRecycler);
        DbContract dbContract = new DbContract(new DbHelper(getActivity()));
        List<CityResponse> cities = dbContract.getAllCities();
        CityListAdapter adapter = new CityListAdapter(getActivity(), this, cities);
        cityRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCityClicked(CityResponse weatherResponse) {
        Toast.makeText(getActivity(), weatherResponse.name, Toast.LENGTH_SHORT).show();
    }
}
