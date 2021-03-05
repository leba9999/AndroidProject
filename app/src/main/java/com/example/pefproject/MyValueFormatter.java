package com.example.pefproject;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

/**
 * Formatoi arvot käyttäen isäntä luokkaa ValueFormatter
 * @author Leevi Koskinen
 * @version 0.01 05.03.2021
 */
public class MyValueFormatter extends ValueFormatter {
    private  ArrayList<String> dates;
    private int countOfDays;

    /**
     * Formatoi XAxisin annetun String Arraylistan mukaan.
     * @param dates asetetaan dates Arraylistalla formatoitavat arvot
     */
    public MyValueFormatter(ArrayList<String> dates){
        this.dates = dates;
        this.countOfDays = dates.size();
    }

    /**
     * Hakee akselin merkin value muuttujan avulla
     * @param value float: X akselin arvo mikä halutaan formatoida
     * @param axis AxisBase: mikä akseli formatoidaan. (Ei vaikuta)
     * @return String palauttaa annetun listan arvon
     */
    @Override
    public String getAxisLabel(float value, AxisBase axis){
        String format = Singleton.getInstance().getDateFormat();
        if ((value >= countOfDays || value < 0))
            return "Val:"+ value;
        return  dates.get(( countOfDays - 1) - (  (int)value)).substring(0, format.length() - 5);
    }
}