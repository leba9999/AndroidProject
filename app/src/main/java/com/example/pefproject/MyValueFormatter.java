package com.example.pefproject;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class MyValueFormatter extends ValueFormatter {
    private  ArrayList<String> dates;
    private int countOfDays;

    /**
     * Formatoi XAxisin annetun String Arraylistan mukaan.
     * @param dates asetetaan dates lista dates arraylistaan
     */
    public MyValueFormatter(ArrayList<String> dates, int countOfDays){
        this.dates = dates;
        this.countOfDays = countOfDays;
    }
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();
        if ((value >= countOfDays || value < 0))
            return "Val:"+ value;
        return  dates.get(( countOfDays - 1) - (  (int)value)).substring(0, format.length() - 5);
    }
}