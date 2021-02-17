package com.example.pefproject;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Record {
    private ArrayList<Integer> normalAirflow;
    private ArrayList<Integer> medicineAirflow;
    private String comment;
    private String date;
    private String time;
    private boolean timeOfDay;

    public Record() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            this.timeOfDay = true;
        }
        this.timeOfDay = false;

        Date date = calendar.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy", Locale.getDefault());
        this.date = dateFormat.format(date);
        this.time = timeFormat.format(date);

        Log.i("RECORD.JAVA", "Current date => " + this.date);
        Log.i("RECORD.JAVA", "Current time => " + this.time);
        Log.i("RECORD.JAVA", "Current am/pm => " + calendar.get(Calendar.AM_PM) + "  am = " + Calendar.AM + " pm = " + Calendar.PM);

    }

    public Record(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public void addNormalAirflow(int AirflowValue) {
        normalAirflow.add(AirflowValue);
    }

    public void addMedicineAirflow(int AirflowValue) {
        medicineAirflow.add(AirflowValue);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Integer> getNormalAirflow() {
        return normalAirflow;
    }

    public ArrayList<Integer> getMedicineAirflow() {
        return medicineAirflow;
    }

    public String getComment() {
        return this.comment;
    }

    public String getTime() {
        return this.time;
    }

    public String getDate() {
        return this.date;
    }
    public boolean getTimeOfDay() {
        return this.timeOfDay;
    }
}