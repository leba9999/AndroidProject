package com.example.pefproject;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyValueFormatter extends ValueFormatter {
    private String [] days = {"Aamu","Ilta"};
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        return days[(int)value % 2];
    }
}