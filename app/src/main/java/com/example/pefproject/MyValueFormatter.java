package com.example.pefproject;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class MyValueFormatter extends ValueFormatter {
    private  ArrayList<String> dates;

    /**
     * Formatoi XAxisin annetun String Arraylistan mukaan.
     * @param dates asetetaan dates lista dates arraylistaan
     */
    public MyValueFormatter(ArrayList<String> dates){
        this.dates = dates;
    }
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();
        if ((value >= dates.size() || value < 0)  || value > 6)
            return dates.get(0).substring(0, format.length() - 5);
        return  dates.get(( dates.size()- 1) - (  (int)value)).substring(0, format.length() - 5);
    }
}