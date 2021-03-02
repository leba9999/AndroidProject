package com.example.pefproject;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyValueFormatter extends ValueFormatter {
    private final String logTag = "com.example.pefproject.APP_MyValueFormatter.java";
    private final String [] days = {"Aamu","Ilta"};
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();
        Log.i(logTag, "Formating value: " + (int)value + " to " + days[(int)value % 2]);

        Date date = Singleton.getInstance().getRecording().get(( Singleton.getInstance().getRecording().size()- 1) - (6 - (int)value)).getDate();
        SimpleDateFormat DateFormat = new SimpleDateFormat(format.substring(0, format.length() - 5), Locale.getDefault());

        return  DateFormat.format(date);
    }
}