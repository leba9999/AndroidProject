package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Luokalla ChartActivity asetetaan ChartActivityyn taulukko joka sisältää Singletonista ladattujen merkintöjen huippu puhallus arvot
 * @author Leevi Koskinen
 * @version 0.01 05.03.2021
 */
public class ChartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView startDateText;
    private TextView endDateText;
    private int year;
    private int month;
    private int day;
    private boolean dayTextView;
    private long dayCount;
    private SimpleDateFormat dateFormat;
    private long day_1;
    private long day_2;
    private Date startDay;
    private Date endDay;
    private Calendar calendar;

    /**
     * Kutsutaan kun aktiviteetti luodaan. Asetetaan kaikille luokan muuttujille arvot
     * @param savedInstanceState sisältää aktiviteetin tallennetun instancen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
       this.year = 0;
       this.month = 0;
       this.day = 0;

        dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        startDateText = findViewById(R.id.startDateTextView);
        endDateText = findViewById(R.id.endDateTextView);
        dayTextView = true;

        calendar = Calendar.getInstance();

        startDay = new Date();
        endDay = new Date();

        calendar.add(Calendar.DATE, -10);
        startDay = calendar.getTime();
        calendar = Calendar.getInstance();
        endDay = calendar.getTime();

        day_1 = startDay.getTime();
        day_2 = endDay.getTime();

        startDateText.setText(dateFormat.format(startDay.getTime()));
        endDateText.setText(dateFormat.format(endDay.getTime()));

        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;
        setUpChart();
    }

    /**
     * Näyttää androidin kalenterin käyttäjälle. Josta käyttäjä voi valita päivämäärän
     * @param date date asettaa kalenterin päivämäärän haluttuun päivään
     */
    public void showDatePickerDialog(Date date){
        calendar.setTimeInMillis(date.getTime());
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Kuuntelinjan DatePickerDialog.OnDateSetListener onDateSet funktio joka kutsutaan kun käyttäjä on valinnut ja hyväksynyt päivämäärän
     * @param view DatePicker: valitsia joka kuuluu avattuun dialogiin
     * @param year int: valittu vuosi
     * @param month int: valittu kuukausi (0-11)
     * @param day int: valittu päivä (1-31, riippuu valitusta kuukaudesta)
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        if (dayTextView){
            calendar.set(this.year, this.month , this.day);
            startDay = calendar.getTime();
            startDateText.setText(dateFormat.format(startDay.getTime()));
            day_1 = calendar.getTimeInMillis();
        } else{
            calendar.set(this.year, this.month , this.day);
            endDay = calendar.getTime();
            endDateText.setText(dateFormat.format(endDay.getTime()));
            day_2 = calendar.getTimeInMillis();
        }
        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;
        setUpChart();
    }

    /**
     * Tutkitaan mitä viewiä on painettu. Jos startDateTextView on painettu niin näytetään kalenteri joka sisältää aloitus päivämäärän.
     * Jos endDateTextView on painettu kalenteri näyttää lopetus päivämäärän.
     * @param view tutkitaan minkä näkymän id:tä on painettu
     */
    public void buttonPressed (View view) {
        if (view.getId() == R.id.startDateTextView){
            showDatePickerDialog(startDay);
            dayTextView = true;
        } else {
            showDatePickerDialog(endDay);
            dayTextView = false;
        }
    }

    /**
     * Asettaa taulukko näkymään palkki taulukon joka sisältää valittujen päivämäärien välisten merkintöjen huippu arvot.
     * Lisää tyhjiä merkintöjä väliin jos Singletonista löydetyt merkinnät eivät täytä kaikkia päivämääriä/kohtia
     */
    private void setUpChart(){
        BarChart barChart = findViewById(R.id.barChartView);

        ArrayList<BarEntry> morningAirflow = new ArrayList<>();
        ArrayList<BarEntry> eveningAirflow = new ArrayList<>();
        ArrayList<BarEntry> extraAirflow = new ArrayList<>();
        ArrayList<Record> records = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Long> dateLong = new ArrayList<>();

        if (Singleton.getInstance().getRecording().isEmpty()){
            return;
        }
        calendar.setTime(startDay);
        dates.add(dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){
            calendar.add(Calendar.DATE, 1);
            if (!dates.contains(dateFormat.format(calendar.getTime()))) {
                dates.add(dateFormat.format(calendar.getTime()));
            }
        }
        for (int i = 0; i < dayCount; i++){
            if (records.isEmpty()){
                Record record = new Record();
                record.setDate(startDay);
                records.add(record);
            }
            for (int s = 0; s < Singleton.getInstance().getRecording().size(); s++) {
                if (dates.get(i).equals(dateFormat.format(Singleton.getInstance().getRecording().get(s).getDate()))) {
                        records.add(Singleton.getInstance().getRecording().get(s));
                } else {
                    Record record = new Record();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(records.get(records.size() - 1).getDate());
                    calendar.add(Calendar.DATE, 1);
                    record.setDate(calendar.getTime());
                    records.add(record);
                }
            }
        }
        // Taulukkoon halutaan 7 päivän tulokset
        for (int i = 0; i < dayCount; i++){
            // Lisätään BarEntryjä jotka siis sisältävät X arvon ja y arvon
            morningAirflow.add(new BarEntry(i, 0));
            eveningAirflow.add(new BarEntry(i, 0));
            extraAirflow.add(new BarEntry(i, 0));
            // asetetaan Airflow listoille arvoksi nollat jotta saadaan kaikkiin väreihin tekstit näkyviin
            // jostain syystyä jos ei anna mitään arvoa niin tekstejä ei näy
            float [] zeroValues = {0,0,};
            morningAirflow.get(i).setVals(zeroValues);
            eveningAirflow.get(i).setVals(zeroValues);
            extraAirflow.get(i).setVals(zeroValues);
            // Käydään kaikki merkinnät läpi ja vertaillaan viimeisempien merkintöjen päivämääriä uusimpien päivämäärien kanssa
            // jos päivämäärät ovat samat lisätään kyseisen päivämäärän merkinnän tiedot taulukkoon values muuttujalla
            for (int d  = 0; d < records.size(); d++){
                //Log.i("APP_PEF", "datesSize: " + dates.size() + " dayCount:" + dayCount+ " tempCount:" + tempCount+ " i:" + i + " KOKO:" + (((int)dayCount - 1) - i));
                if (dateFormat.format(records.get(d).getDate()).equals(dates.get((  (((int)dayCount - 1) - i))))) {
                    float [] values = {
                            records.get(d).getPeakNormalAirflow(),
                            records.get(d).getPeakMedicineAirflow(),
                    };
                    // Tarkastetaan vielä minkä tyyppinen merkintä on kyseessä jotta tiedot menevät oikealle palkille
                    switch (records.get(d).getType()){
                        case Record.AM:
                            morningAirflow.get(i).setVals(values);
                            break;
                        case Record.PM:
                            eveningAirflow.get(i).setVals(values);
                            break;
                        case Record.EXTRA:
                            extraAirflow.get(i).setVals(values);
                            break;
                    }
                }
            }
        }
        int [] morningColors = {
                ContextCompat.getColor(this, R.color.white_230) ,
                ContextCompat.getColor(this, R.color.light_blue)
        };
        int [] eveningColors = {
                ContextCompat.getColor(this, R.color.grey) ,
                ContextCompat.getColor(this, R.color.dark_blue)
        };
        int [] extraColors = {
                ContextCompat.getColor(this, R.color.light_yellow) ,
                ContextCompat.getColor(this, R.color.yellow)
        };
        String [] colorLabels = {
                getString(R.string.Normal) ,
                getString(R.string.Medicine)
        };
        BarDataSet morningDataSet = new BarDataSet(morningAirflow, "");
        morningDataSet.setColors(morningColors);
        morningDataSet.setStackLabels(colorLabels);
        morningDataSet.setValueTextSize(12f);

        BarDataSet eveningDataSet = new BarDataSet(eveningAirflow, "");
        eveningDataSet.setColors(eveningColors);
        eveningDataSet.setStackLabels(colorLabels);
        eveningDataSet.setValueTextSize(12f);

        BarDataSet extraDataSet = new BarDataSet(extraAirflow, "");
        extraDataSet.setColors(extraColors);
        extraDataSet.setStackLabels(colorLabels);
        extraDataSet.setValueTextSize(12f);

        BarData barData = new BarData(morningDataSet, eveningDataSet, extraDataSet);

        barData.setBarWidth(1f / 3f);

        barChart.getXAxis().setCenterAxisLabels(true);

        barChart.setData(barData);
        barChart.groupBars(0f, 0f, 0f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(dayCount);
        barChart.setPinchZoom(true);

        barChart.setScaleYEnabled(false);
        barChart.setScaleXEnabled(true);

        barChart.getXAxis().setValueFormatter(new MyValueFormatter(dates));
        barChart.getDescription().setText(getString(R.string.PeakAirflow));
        barChart.setDrawValueAboveBar(false);

        barChart.invalidate();
    }
}