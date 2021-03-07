package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
public class ChartActivity extends AppCompatActivity {

    private TextView startDateText;
    private TextView endDateText;

    private boolean dayTextView;

    // Kuinka monta päivää on aloitus ja lopetus päivien välissä
    private long dayCount;

    private SimpleDateFormat dateFormat;

    // Valitut päivämäärät
    private Date startDay;
    private Date endDay;

    private Calendar calendar;

    /**
     * Kutsutaan kun aktiviteetti luodaan. Asetetaan kaikille luokan muuttujille arvot.
     * Asetetaan setOnClickListener funktiot TextView:ille ja setOnDateSetListener DatePickerDialog:ille.
     * @param savedInstanceState sisältää aktiviteetin tallennetun instancen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // asetetaan SimpleDateFormatille formatointi muoto joka on määritelty Singletonissa
        dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        // Haetaan textView:en id:t
        startDateText = findViewById(R.id.startDateTextView);
        endDateText = findViewById(R.id.endDateTextView);

        //
        dayTextView = true;

        calendar = Calendar.getInstance();

        startDay = new Date();
        endDay = new Date();

        calendar.add(Calendar.DATE, -10);
        startDay = calendar.getTime();
        calendar = Calendar.getInstance();
        endDay = calendar.getTime();

        startDateText.setText(dateFormat.format(startDay.getTime()));
        endDateText.setText(dateFormat.format(endDay.getTime()));

        dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        setUpChart();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        startDateText.setOnClickListener(v -> {
            calendar.setTime(startDay);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            dayTextView = true;
        });
        endDateText.setOnClickListener(v -> {
            calendar.setTime(endDay);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            dayTextView = false;
        });
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            if (dayTextView){
                startDay = calendar.getTime();
                startDateText.setText(dateFormat.format(startDay.getTime()));
            } else {
                endDay = calendar.getTime();
                endDateText.setText(dateFormat.format(endDay.getTime()));
            }
            dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;
            setUpChart();
        });
    }

    /**
     * Asettaa taulukko näkymään palkki taulukon joka sisältää valittujen päivämäärien välisten merkintöjen huippu arvot.
     * Lisää tyhjiä merkintöjä väliin jos Singletonista löydetyt merkinnät eivät täytä kaikkia päivämääriä/kohtia
     */
    private void setUpChart(){
        // Haetaan barChartView barChartille
        BarChart barChart = findViewById(R.id.barChartView);

        // Luodaan väliaikaiset Listat arvoja varten
        ArrayList<BarEntry> morningAirflow = new ArrayList<>();
        ArrayList<BarEntry> eveningAirflow = new ArrayList<>();
        ArrayList<BarEntry> extraAirflow = new ArrayList<>();

        // Luodaan uusi merkintöjen lista jotta voidaan lisätä uusia tyhjiä merkintöjä
        // ilman että Singletonissa oleva lista kasvaisi
        ArrayList<Record> records = new ArrayList<>();

        // Luodaan lista joka sisältää aloitus päivän ja lopetus päivän väliset päivämäärät
        ArrayList<String> dates = new ArrayList<>();

        // Jos Singletonin merkintä lista on tyhjä niin ihan turhaa yritettään laittaa tietoa taulukkoon
        // niin palataan suosiolla takasin
        if (Singleton.getInstance().getRecording().isEmpty()){
            return;
        }
        // asetetaan kalenterille aika sama kuin aloitus päivä jotta voidaan sitten lisätä aina yksi päivä lisää kunnes päästään
        // lopetus päivämäärään
        calendar.setTime(startDay);
        // lisätään ensimmäinen päivämäärä dates listaan
        dates.add(dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){
            // Kalenterin päivämäärää nostetaan aina yhdellä jotta saadaan uusi päivämäärä dates listaan
            calendar.add(Calendar.DATE, 1);
            // jos dates lista sisältää jo kyseisen päivämäärän niin ei lisätä sitä koska dates ei saa sisältää kopioita
            // ja dates  listan pitää olla päivä järjestyksessä
            if (!dates.contains(dateFormat.format(calendar.getTime()))) {
                dates.add(dateFormat.format(calendar.getTime()));
            }
        }
        // loopataan päivät läpi
        for (int i = 0; i < dayCount; i++){
            // jos records on tyhjä lisätään sinne ensimmäinen arvo
            if (records.isEmpty()){
                Record record = new Record();
                record.setDate(startDay);
                records.add(record);
            }
            // loopataan Singletonissa olevat merkinnät läpi
            for (int s = 0; s < Singleton.getInstance().getRecording().size(); s++) {
                // Tarkastetaan onko kyseinen päivä sama kuin Singletonissa olevan merkinnän
                // jos on niin lisätään se records listaan
                if (dates.get(i).equals(dateFormat.format(Singleton.getInstance().getRecording().get(s).getDate()))) {
                        records.add(Singleton.getInstance().getRecording().get(s));
                } else {
                    // Uusi tyhjä merkintä jolle asetetaan päivämääräksi edellisen merkinnän päivämäärä + 1
                    // ja lisätään merkintä records listaan
                    Record record = new Record();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(records.get(records.size() - 1).getDate());
                    calendar.add(Calendar.DATE, 1);
                    record.setDate(calendar.getTime());
                    records.add(record);
                }
            }
        }
        // Taulukkoon halututujen päivien määrä käydään läpi
        for (int i = 0; i < dayCount; i++){
            // Lisätään BarEntryjä jotka siis sisältävät X arvon ja y arvon
            morningAirflow.add(new BarEntry(i, 0));
            eveningAirflow.add(new BarEntry(i, 0));
            extraAirflow.add(new BarEntry(i, 0));

            // asetetaan Airflow listoille arvoksi nollat jotta saadaan kaikkiin väreihin tekstit näkyviin
            // jostain syystyä jos ei anneta mitään arvoa niin tekstejä ei näy
            float [] zeroValues = {0,0,};
            morningAirflow.get(i).setVals(zeroValues);
            eveningAirflow.get(i).setVals(zeroValues);
            extraAirflow.get(i).setVals(zeroValues);

            // Käydään kaikki merkinnät läpi ja vertaillaan viimeisempien merkintöjen päivämääriä uusimpien päivämäärien kanssa
            // jos päivämäärät ovat samat lisätään kyseisen päivämäärän merkinnän tiedot taulukkoon values muuttujalla
            for (int d  = 0; d < records.size(); d++){
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
        // Talukon värit
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
        // aamu merkintöjen arvot
        BarDataSet morningDataSet = new BarDataSet(morningAirflow, "");
        morningDataSet.setColors(morningColors);
        morningDataSet.setStackLabels(colorLabels);
        morningDataSet.setValueTextSize(12f);

        // ilta merkintöjen arvot
        BarDataSet eveningDataSet = new BarDataSet(eveningAirflow, "");
        eveningDataSet.setColors(eveningColors);
        eveningDataSet.setStackLabels(colorLabels);
        eveningDataSet.setValueTextSize(12f);

        // ylimääräisten merkintöjen arvot
        BarDataSet extraDataSet = new BarDataSet(extraAirflow, "");
        extraDataSet.setColors(extraColors);
        extraDataSet.setStackLabels(colorLabels);
        extraDataSet.setValueTextSize(12f);

        // BarData sisältää BarDataSetejen tiedot
        BarData barData = new BarData(morningDataSet, eveningDataSet, extraDataSet);
        // Leveys määrittellään X mukaan ja arvo voi olla (1f-0f)%
        barData.setBarWidth(1f / 3f);

        // Varmistetaan että taulukon palkit on X merkien keskellä
        barChart.getXAxis().setCenterAxisLabels(true);
        // Annetaan barChartille data
        barChart.setData(barData);
        // ilmoitetaan ryhmä palkkien välit
        barChart.groupBars(0f, 0f, 0f);
        // Pienin X akselin arvo minkä voi näyttää. Ei mennä negatiivisen puolelle
        barChart.getXAxis().setAxisMinimum(0);
        // Suurin X akselin arvo minkä voi näyttää. Näytetään vain valittujen päivämäärien suurinpaan arvoon asti
        barChart.getXAxis().setAxisMaximum(dayCount);
        // Zoom ja X,Y skaalaus
        // Formatoidaan X akseli päivämäärien mukaan
        barChart.getXAxis().setValueFormatter(new MyValueFormatter(dates));
        // asetetaan taulukon kuvaus
        barChart.getDescription().setText(getString(R.string.PeakAirflow));
        // siiretään arvojen paikka palkkien alapuolelle
        barChart.setDrawValueAboveBar(false);
        // päivitetään taulukko
        barChart.invalidate();
    }
}