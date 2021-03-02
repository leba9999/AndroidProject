package com.example.pefproject;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Luokka sisältää merkinnälle kaiken tarpeellisen datan. Esim pv, aika, kommentti ja ilmankulun arvot
 * @author Leevi Koskinen
 * @version 0.01 1.3.2021
 */
public class Record {
    private final String logTag = "com.example.pefproject.APP_Record.java";
    private ArrayList<Integer> normalAirflow;
    private ArrayList<Integer> medicineAirflow;

    private String comment;

    private String timeFormat;
    private String dateFormat;

    private Date date;
    private boolean timeOfDay;

    /**
     * Record constructor asettaa tämän hetkisen päivämäärän ja kellon ajan kalenterin avulla
     * Luo myöskin uudet ArrayListit normal- ja medicineAirflow listoille
     */
    public Record() {
        normalAirflow = new ArrayList<>();
        medicineAirflow = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            this.timeOfDay = true;
        } else {
            this.timeOfDay = false;
        }
        date = calendar.getTime();

        Log.i(logTag, "Current date => " + date);
    }

    /**
     * Lisää normalAirflow Arraylistiin uuden arvon joka annetaan parametrissä
     * @param AirflowValue Normaali puhalluksen arvo
     */
    public void addNormalAirflow(int AirflowValue) {
        this.normalAirflow.add(AirflowValue);
    }

    /**
     * Lisää medicineAirflow Arraylistiin uuden arvon joka annetaan parametrissä
     * @param AirflowValue Lääke puhalluksen arvo
     */
    public void addMedicineAirflow(int AirflowValue) {
        this.medicineAirflow.add(AirflowValue);
    }

    /**
     * Asetetaan merkinnän vapaa ehtoinen kommentti
     * @param comment Vapaa muotoinen kommentti teksti
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /*
     * Asetetaan aika tarvittaessa
     * @param time uusi aika arvo.
     */
    /*
    public void setTime(String time) {
        this.time = time;
    }
    */

    /*
     * Asetetaan päivämäärä tarvittaessa
     * @param date uusi päivämäärä arvo.
     */
    /*
    public void setDate(String date) {
        this.date = date;
    }
    */

    /**
     * haetaan normalAirflown ArrayList
     * palauttaa normalAirflow arraylistin
     * @return ArrayList<Integer> normalAirflow
     */
    public ArrayList<Integer> getNormalAirflowList() {
        return this.normalAirflow;
    }

    /**
     * haetaan medicineAirflow ArrayList
     * palauttaa medicineAirflow Arraylistin
     * @return ArrayList<Integer> medicineAirflow
     */
    public ArrayList<Integer> getMedicineAirflowList() {
        return this.medicineAirflow;
    }

    /**
     * haetaan huippu arvo normalAirflow ArrrayListasta
     * palauttaa huippu arvon
     * @return int tempPeak
     */
    public int getPeakNormalAirflow(){
        int tempPeak = 0;
        for (Integer number : this.normalAirflow) {
            if(number >= tempPeak){
                tempPeak = number;
            }
        }
        return tempPeak;
    }

    /**
     * haetaan huippu arvo medicineAirflow ArrrayListasta
     * palauttaa huippu arvon
     * @return int tempPeak
     */
    public int getPeakMedicineAirflow(){
        int tempPeak = 0;
        for (Integer number : this.medicineAirflow) {
            if(number >= tempPeak){
                tempPeak = number;
            }
        }
        return tempPeak;
    }

    /**
     * Haetaan merkinnän kommentti
     * palauttaa merkinnän kommentin
     * @return String comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Haetaan merkinnän aika
     * palauttaa merkinnän ajan
     * @return String time
     */
    public String getTime() {
        SimpleDateFormat TimeFormat = new SimpleDateFormat(this.timeFormat, Locale.getDefault());
        return TimeFormat.format(this.date);
    }
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Haetaan merkinnän päivämäärä
     * palauttaa merkinnän päivämäärän
     * @return String date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Käytännössä PM tai AM. Eli onko merkintä tehty aamulla vai illalla.
     * true == PM && false == AM
     * @return boolean timeOfDay
     */
    public boolean getTimeOfDay() {
        return this.timeOfDay;
    }

    @Override
    public String toString() {
        SimpleDateFormat TimeFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());
        SimpleDateFormat DateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return " Normal: " + getPeakNormalAirflow() + " Medicine: " + getPeakMedicineAirflow()+ " Date: " + DateFormat.format(date) + " Time: " + TimeFormat.format(date);
    }
}