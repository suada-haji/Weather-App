package com.suadahaji.weatherapp.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.adapters.CityListAdapter;
import com.suadahaji.weatherapp.data.CityResponse;
import com.suadahaji.weatherapp.data.database.DbContract;
import com.suadahaji.weatherapp.data.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class CityListFragment extends Fragment implements CityListAdapter.ItemClickListener {
    private ActionMode cityActionMode;
    private DbContract dbContract;
    private List<CityResponse> cities = new ArrayList<>();
    private CityListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView cityRecyclerView = view.findViewById(R.id.cityListRecycler);
        dbContract = new DbContract(new DbHelper(getActivity()));
        cities = dbContract.getAllCities();
        adapter = new CityListAdapter(getActivity(), this, cities);
        cityRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCityClicked(CityResponse cityResponse) {
        Toast.makeText(getActivity(), cityResponse.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCityLongClicked(final CityResponse cityResponse) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Do you want to delete this city?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbContract.deleteBookmarkedCity(cityResponse);
                        cities.remove(cityResponse);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create();
        alertDialogBuilder.show();

        return true;
    }

}
