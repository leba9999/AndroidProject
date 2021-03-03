package com.example.pefproject;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyValueFormatter extends ValueFormatter {
    private final String logTag = "com.example.pefproject.APP_MyValueFormatter.java";
    public MyValueFormatter(){

    }
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();

       // Log.i(logTag, " Value: " + value + " Size: " + Singleton.getInstance().getRecording().size());

        SimpleDateFormat DateFormat = new SimpleDateFormat(format.substring(0, format.length() - 5), Locale.getDefault());

        if (value >= Singleton.getInstance().getRecording().size() || value < 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size()- 1).getDate());
            calendar.add(Calendar.DATE, 1);
            return DateFormat.format(calendar.getTime());
        }
        return "jee";
        //Date date = Singleton.getInstance().getRecording().get(( Singleton.getInstance().getRecording().size()- 1) - (6 - (int)value)).getDate();
        //return  DateFormat.format(date);
    }
}