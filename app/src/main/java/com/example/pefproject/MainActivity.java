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

/**
 * Luokalla MainActivity asetetaan: taulukko joka sisältää viikon merkintöjen huippu arvot jotka on Singletonista ladattu,
 * haetaan Singletonista tämän hetkisen päivämäärän merkintöjen huippu arvot texView:hin
 * ja liikutaan muihin aktiviteetteihin
 * @author Leevi Koskinen
 * @version 0.01 05.03.2021
 */
public class MainActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_MainActivity.java";
    private TextView morningTextView;
    private TextView eveningTextView;
    private TextView extraTextView;
    private TextView recordTextView;

    private String normalFirstLetter;
    private String medicineFirstLetter;

    private  SimpleDateFormat dateFormat;

    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.getInstance().loadData(this);
        //setTestRecords();

        calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        this.morningTextView = findViewById(R.id.morningTextView);
        this.eveningTextView = findViewById(R.id.eveningTextView);
        this.extraTextView = findViewById(R.id.extraTextView);
        this.recordTextView = findViewById(R.id.recordTextView);

        normalFirstLetter = getString(R.string.Normal).substring(0, getString(R.string.Normal).length()-(getString(R.string.Normal).length() - 1));
        medicineFirstLetter = getString(R.string.Medicine).substring(0, getString(R.string.Medicine).length()-(getString(R.string.Medicine).length() - 1));

        recordTextView.setText(getString(R.string.Record) + ": " + dateFormat.format(date));
        morningTextView.setText(getString(R.string.Morning) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");
        eveningTextView.setText(getString(R.string.Evening) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");
        extraTextView.setText(getString(R.string.Extra) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");

        setUpChart();
        loadDayRecord();
    }

    /**
     * Lataa päivän huippu arvot näkyviin MainActivityssä
     */
    private void loadDayRecord(){
        ArrayList<Record> records = Singleton.getInstance().getRecording();
        if (records.isEmpty()){
            return;
        }
        calendar = Calendar.getInstance();
        for (int i = 0; i < records.size(); i++){
            if (dateFormat.format(records.get(i).getDate()).equals(dateFormat.format(calendar.getTime()))){
                if (records.get(i).getType() == Record.PM){
                    eveningTextView.setText(getString(R.string.Evening) +":\n"
                            + normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == Record.AM) {
                    morningTextView.setText(getString(R.string.Morning) +":\n"
                            + normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == Record.EXTRA) {
                    extraTextView.setText(getString(R.string.Extra) +":\n"
                            + normalFirstLetter + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + medicineFirstLetter + ": " + records.get(i).getPeakMedicineAirflow());
                }
            }
        }
    }

    /**
     * Asettaa taulukolle viikon puhallusten huippu arvot ja värit
     */
    private void setUpChart(){
        BarChart barChart = findViewById(R.id.barView);

        ArrayList<BarEntry> morningAirflow = new ArrayList<>();
        ArrayList<BarEntry> eveningAirflow = new ArrayList<>();
        ArrayList<BarEntry> extraAirflow = new ArrayList<>();
        ArrayList<Record> records = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        if (Singleton.getInstance().getRecording().isEmpty()){
            return;
        }
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);
        Date startDay = calendar.getTime();
        calendar = Calendar.getInstance();
        Date endDay = calendar.getTime();
        calendar.setTime(startDay);
        long dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;

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
        for (int i = 0; i < dayCount; i++){

            morningAirflow.add(new BarEntry(i, 0));
            eveningAirflow.add(new BarEntry(i, 0));
            extraAirflow.add(new BarEntry(i, 0));

            float [] zeroValues = {0,0,};
            morningAirflow.get(i).setVals(zeroValues);
            eveningAirflow.get(i).setVals(zeroValues);
            extraAirflow.get(i).setVals(zeroValues);

            for (int d  = 0; d < records.size(); d++){

                if (dateFormat.format(records.get(d).getDate()).equals(dates.get((  (((int)dayCount - 1) - i))))) {
                    float [] values = {
                            records.get(d).getPeakNormalAirflow(),
                            records.get(d).getPeakMedicineAirflow(),
                    };

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

    /**
     * Tutkitaan mitä näkymää painettiin ja tehdään tarvittavat toimen piteet
     * @param view haetaan painetun näkymän id arvo view:llä
     */
    public void buttonPressed (View view) {
        Intent intent;
        if (view.getId() == R.id.NewRecordButton){
            intent = new Intent(this, NewRecordActivity.class);
            intent.putExtra(OldRecordActivity.EXTRA, -1);
            startActivity(intent);
        } else if (view.getId() == R.id.ChartButton){
            intent = new Intent(this, ChartActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, OldRecordActivity.class);
            startActivity(intent);
        }
    }
    private void setTestRecords(){
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 10; i++) {
                Singleton.getInstance().addRecord(new Record());

                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(130 + j * 5 + 10 * i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(145 + j * 5 + 10 * i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(140 + j * 5 + 10 * i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(222 + j * 5 + 10 * i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(212 + j * 5 + 10 * i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(232 + j * 5 + 10 * i);

                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).setType(j);
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -i);
                Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).setDate(calendar.getTime());
            }
        }
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 15);
        Singleton.getInstance().getRecording().get(4).setDate(calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        Singleton.getInstance().getRecording().get(8).setDate(calendar.getTime());
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
        super.onResume();
    }
}