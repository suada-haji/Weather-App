package com.suadahaji.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suadahaji.weatherapp.R;
import com.suadahaji.weatherapp.data.model.CityResponse;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {
    private List<CityResponse> cities;
    private ItemClickListener listener;

    public CityListAdapter(Context context, ItemClickListener clickListener, List<CityResponse> cities) {
        this.listener = clickListener;
        this.cities = cities;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, final int position) {
        holder.cityName.setText(cities.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCityClicked(cities.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setSelected(true);
                return listener.onCityLongClicked(cities.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public interface ItemClickListener {
        void onCityClicked(CityResponse weatherResponse);
        boolean onCityLongClicked(CityResponse weatherResponse);
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
        }
    }
}
