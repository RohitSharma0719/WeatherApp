package com.example.openweatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherHolder extends RecyclerView.ViewHolder {
    TextView minmax;
    TextView uv;
    TextView description;
    TextView percip;
    TextView morning,day,evening,night;
    ImageView imageView;
    TextView daily;
    public WeatherHolder(@NonNull View itemView) {
        super(itemView);
        uv=itemView.findViewById(R.id.uvindex);
        minmax=itemView.findViewById(R.id.minmaxTemp);
        description=itemView.findViewById(R.id.desc);
        percip=itemView.findViewById(R.id.precip);
        morning=itemView.findViewById(R.id.mortemp);
        day=itemView.findViewById(R.id.daytemp);
        evening=itemView.findViewById(R.id.evetemp);
        night=itemView.findViewById(R.id.nighttemp);
        imageView=itemView.findViewById(R.id.pic);
        daily=itemView.findViewById(R.id.day);
    }
}

