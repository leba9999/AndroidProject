package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Luokalla MainActivity asetetaan: taulukko joka sisältää viikon merkintöjen huippu arvot jotka on Singletonista ladattu,
 * haetaan Singletonista tämän hetkisen päivämäärän merkintöjen huippu arvot texView:hin
 * ja liikutaan muihin aktiviteetteihin
 * @author Leevi Koskinen
 * @version 0.01 05.03.2021
 */
public class MainActivity extends AppCompatActivity {
    private TextView morningTextView;
    private TextView eveningTextView;
    private TextView extraTextView;
    private TextView recordTextView;

    private String normalFirstLetter;
    private String medicineFirstLetter;

    private  SimpleDateFormat dateFormat;

    private Calendar calendar;
    /**
     * Kutsutaan kun aktiviteetti luodaan. Asetetaan kaikille luokan muuttujille arvot
     * @param savedInstanceState sisältää aktiviteetin tallennetun instancen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.getInstance().loadData(this);

        this.calendar = Calendar.getInstance();
        // luodaan uusi SimpleDateFormat olio Singletonissa olevalla formaatilla
        this.dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        // Haetaan viewit id:llä
        this.morningTextView = findViewById(R.id.morningTextView);
        this.eveningTextView = findViewById(R.id.eveningTextView);
        this.extraTextView = findViewById(R.id.extraTextView);
        this.recordTextView = findViewById(R.id.recordTextView);

        // Hankitaan Normaalin ja Lääkeen ensimmäinen kirjain jotta voidaan laittaa se textviewiin (Normaali: 340 -> N: 340)
        this.normalFirstLetter = getString(R.string.Normal).substring(0, getString(R.string.Normal).length()-(getString(R.string.Normal).length() - 1));
        this.medicineFirstLetter = getString(R.string.Medicine).substring(0, getString(R.string.Medicine).length()-(getString(R.string.Medicine).length() - 1));

        // Asetetaan tekstit TextVieweihin.
        this.recordTextView.setText(getString(R.string.Record) + ": " + this.dateFormat.format(calendar.getTime()));
        this.morningTextView.setText(getString(R.string.Morning) +":\n"+ this.normalFirstLetter + ":---\n" +this.medicineFirstLetter + ":---");
        this.eveningTextView.setText(getString(R.string.Evening) +":\n"+ this.normalFirstLetter + ":---\n" + this.medicineFirstLetter + ":---");
        this.extraTextView.setText(getString(R.string.Extra) +":\n"+ this.normalFirstLetter + ":---\n" + this.medicineFirstLetter + ":---");

        this.setUpChart();
        this.loadDayRecord();
    }

    /**
     * Lataa päivän huippu arvot Singletonissta näkyviin MainActivityn TextView laatikoihin
     */
    private void loadDayRecord(){
        ArrayList<Record> records = Singleton.getInstance().getRecording();
        if (records.isEmpty()){
            return;
        }
        // Varmistetaan että kalenterilla on tämä päivä tällä hetkellä
        this.calendar = Calendar.getInstance();
        // Käydääm merkinnät läpi ja lisätään niiden merkintöjen arvot textview:n jotka vastaavat tätä päivää
        for (int i = 0; i < records.size(); i++){
            if (this.dateFormat.format(records.get(i).getDate()).equals(this.dateFormat.format(this.calendar.getTime()))){
                if (records.get(i).getType() == Record.PM){
                    this.eveningTextView.setText(getString(R.string.Evening) +":\n"
                            + this.normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + this.medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == Record.AM) {
                    this.morningTextView.setText(getString(R.string.Morning) +":\n"
                            + this.normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + this.medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == Record.EXTRA) {
                    this.extraTextView.setText(getString(R.string.Extra) +":\n"
                            + this.normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + this.medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }
            }
        }
    }

    /**
     * Asettaa taulukko näkymään palkki taulukon joka sisältää viikon merkintöjen huippu arvot.
     * Lisää tyhjiä merkintöjä väliin jos Singletonista löydetyt merkinnät eivät täytä kaikkia päivämääriä/kohtia
     * Asettaa aamu-,ilta- ja ylimääräisille merkinnöille värit
     */
    private void setUpChart(){
        // Haetaan barChartView barChartille
        BarChart barChart = findViewById(R.id.barView);

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

        this.calendar = Calendar.getInstance();
        this.calendar.add(Calendar.DATE, -6);
        Date startDay = this.calendar.getTime();
        this.calendar = Calendar.getInstance();
        Date endDay = this.calendar.getTime();
        this.calendar.setTime(startDay);
        // lasketaan monta päivää on aloitus ja lopetus päivämäärien välissä
        long dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;

        // lisätään ensimmäinen päivämäärä dates listaan
        dates.add(this.dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){
            // Kalenterin päivämäärää nostetaan aina yhdellä jotta saadaan uusi päivämäärä dates listaan
            this.calendar.add(Calendar.DATE, 1);
            // jos dates lista sisältää jo kyseisen päivämäärän niin ei lisätä sitä koska dates ei saa sisältää kopioita
            // ja dates  listan pitää olla päivä järjestyksessä
            if (!dates.contains(this.dateFormat.format(this.calendar.getTime()))) {
                dates.add(this.dateFormat.format(this.calendar.getTime()));
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
                if (dates.get(i).equals(this.dateFormat.format(Singleton.getInstance().getRecording().get(s).getDate()))) {
                    records.add(Singleton.getInstance().getRecording().get(s));
                } else {
                    // Uusi tyhjä merkintä jolle asetetaan päivämääräksi edellisen merkinnän päivämäärä + 1
                    // ja lisätään merkintä records listaan
                    Record record = new Record();
                    this.calendar = Calendar.getInstance();
                    this.calendar.setTime(records.get(records.size() - 1).getDate());
                    this.calendar.add(Calendar.DATE, 1);
                    record.setDate(this.calendar.getTime());
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
                //Log.i("APP_PEF", "datesSize: " + dates.size() + " dayCount:" + dayCount+ " tempCount:" + tempCount+ " i:" + i + " KOKO:" + (((int)dayCount - 1) - i));
                if (this.dateFormat.format(records.get(d).getDate()).equals(dates.get((dates.size() - 1) - i))) {
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
        barChart.setPinchZoom(true);
        barChart.setScaleYEnabled(false);
        barChart.setScaleXEnabled(true);
        // Formatoidaan X akseli päivämäärien mukaan
        barChart.getXAxis().setValueFormatter(new MyValueFormatter(dates));
        // asetetaan taulukon kuvaus
        barChart.getDescription().setText(getString(R.string.PeakAirflow));
        // siiretään arvojen paikka palkkien alapuolelle
        barChart.setDrawValueAboveBar(false);
        // päivitetään taulukko
        barChart.invalidate();
    }

    /**
     * Tutkitaan mitä näkymää painettiin ja tehdään tarvittavat toimen piteet
     * @param view haetaan painetun näkymän id arvo view:llä
     */
    public void buttonPressed (View view) {
        Intent intent;
        if (view.getId() == R.id.NewRecordButton){
            intent = new Intent(this, NewRecordActivity.class);
            intent.putExtra(OldRecordActivity.EXTRA, -1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else if (view.getId() == R.id.ChartButton){
            intent = new Intent(this, ChartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            intent = new Intent(this, OldRecordActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Kun aktiviteetti pysäytetään tallenetaan Singletonin arvot käyttäen singletonin saveData() funktiota
     */
    @Override
    protected void onPause() {
        Singleton.getInstance().saveData(this);
        super.onPause();
    }

    /**
     * Kun aktiviteetti palaa takaisin toimintaan ladataan Singletonin arvot käyttäen singletonin loadData() funktiota
     */
    @Override
    protected void onResume() {
        Singleton.getInstance().loadData(this);
        loadDayRecord();
        setUpChart();
        super.onResume();
    }
}