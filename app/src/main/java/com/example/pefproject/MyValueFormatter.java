package com.example.pefproject;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyValueFormatter extends ValueFormatter {
    private final String logTag = "com.example.pefproject.APP_MyValueFormatter.java";
    private String [] days = {"Aamu","Ilta"};
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        Log.i(logTag, "Formating value: " + (int)value + " to " + days[(int)value % 2]);
        return days[(int)value % 2];
    }
}