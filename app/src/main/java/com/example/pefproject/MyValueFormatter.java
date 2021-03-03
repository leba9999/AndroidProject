package com.example.pefproject;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyValueFormatter extends ValueFormatter {
    private final String logTag = "com.example.pefproject.APP_MyValueFormatter.java";
    ArrayList<String> dates;
    public MyValueFormatter(ArrayList<String> dates){
        this.dates = dates;
    }
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();

       Log.i(logTag, " Value: " + value + " Size: " + dates.size());

        SimpleDateFormat DateFormat = new SimpleDateFormat(format.substring(0, format.length() - 5), Locale.getDefault());
        if ((value >= dates.size() || value < 0)  || value > 6) {
            return dates.get(0).substring(0, format.length() - 5);
        }
        /*
        if ((value >= dates.size() || value < 0)  || value > 6){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( dates.get(dates.size()- 1).getDate());
            calendar.add(Calendar.DATE, 1);
            return DateFormat.format(calendar.getTime());
        }
        */
        Log.i(logTag, " Value: " + value + " Size: " + dates.size() + "  CAl: " + (( dates.size()- 1) - ( ( dates.size()- 1) - (int)value)));
        //Date date = dates.get(( dates.size()- 1) - (  (int)value)).getDate();
        //Log.i(logTag, " END");
        return  dates.get(( dates.size()- 1) - (  (int)value)).substring(0, format.length() - 5);
    }
}