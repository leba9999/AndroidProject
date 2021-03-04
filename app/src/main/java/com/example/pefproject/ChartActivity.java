package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        startDateText = findViewById(R.id.startDateTextView);
        endDateText = findViewById(R.id.endDateTextView);
        dayTextView = true;

        Calendar calendar = Calendar.getInstance();
        startDateText.setText(dateFormat.format(calendar.getTime()));
        Date date = calendar.getTime();
        Log.i("APP_PEF", "1 Calendar:" + calendar.getTimeInMillis());
        day_1 = date.getTime();
        calendar.add(Calendar.DATE, 10);
        date = calendar.getTime();
        endDateText.setText(dateFormat.format(calendar.getTime()));
        day_2 = date.getTime();

        Log.i("APP_PEF", "2 Calendar:" + calendar.getTimeInMillis());
        Log.i("APP_PEF", "3 Calendar:" + ((day_2 - day_1) / (24 * 60 * 60 * 1000)));
        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;
        setUpChart();
    }
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month , this.day);

        if (dayTextView){
            startDateText.setText(dateFormat.format(calendar.getTime()));
            day_1 = calendar.getTimeInMillis();
        } else{
            endDateText.setText(dateFormat.format(calendar.getTime()));
            day_2 = calendar.getTimeInMillis();
        }
        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;
        setUpChart();
    }
    public void buttonPressed (View view) {
        switch (view.getId()) {
            // Buttons id:
            case R.id.startDateTextView:
                showDatePickerDialog();
                dayTextView = true;
                break;
            case R.id.endDateTextView:
                showDatePickerDialog();
                dayTextView = false;
                break;
            default:
                break;
        }
    }

    private void setUpChart(){
        Log.i("APP_PEF", "Start");
        BarChart barChart = findViewById(R.id.barChartView);

        ArrayList<BarEntry> morningAirflow = new ArrayList<>();
        ArrayList<BarEntry> eveningAirflow = new ArrayList<>();
        ArrayList<BarEntry> extraAirflow = new ArrayList<>();
        ArrayList<Record> records = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        if (Singleton.getInstance().getRecording().isEmpty()){
            return;
        }
        // Käydään läpi singletonissa olevat merkinnät ja lisätään ne records listaan jotta voidaan
        // ongelmitta lisätä tyhjiä merkintöjä tarvittaessa ilman että ne tallentuu Singletoniin
        for (int x = 0; x < Singleton.getInstance().getRecording().size(); x++){
            records.add(Singleton.getInstance().getRecording().get(x));
            // jos dates lista on tyhjä lisätään sinne päivämäärä
            if (dates.isEmpty()){
                dates.add(dateFormat.format(records.get(x).getDate()));
                continue;
            }
            // tarkastetaan sisältääkö dates jo kyseisen päivämäärän jos ei niin lisätään se listaan
            if (!dates.contains(dateFormat.format(records.get(x).getDate()))){
                dates.add(dateFormat.format(records.get(x).getDate()));
            }
        }
        // jos uniikkeja päivämääriä ei ollut tarpeeksi singletonissa tallennettuna tarvitaan lisätä
        // tyhjiä merkintöjä jotka sisältävät uniikin päivämäärän
        while (dates.size() <= dayCount){
            Record record = new Record();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(records.get(records.size()-1).getDate());
            calendar.add(Calendar.DATE, 1);
            record.setDate(calendar.getTime());
            records.add(record);
            // jos vahingossa jollain merkinnällä onkin sama päivämäärä niin varmisteaan se ja ei lisätä kopiota
            if (!dates.contains(dateFormat.format(records.get(records.size()-1).getDate()))){
                dates.add(dateFormat.format(records.get(records.size()-1).getDate()));
            }
        }
        Log.i("APP_PEF", "datesSize: " + dates.size() + " Count" + dayCount);
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
                if (dateFormat.format(records.get(d).getDate()).equals(dates.get(((int)dayCount - 1) - i))) {
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

        BarDataSet eveningDataSet = new BarDataSet(eveningAirflow, "");
        eveningDataSet.setColors(eveningColors);
        eveningDataSet.setStackLabels(colorLabels);

        BarDataSet extraDataSet = new BarDataSet(extraAirflow, "");
        extraDataSet.setColors(extraColors);
        extraDataSet.setStackLabels(colorLabels);

        BarData barData = new BarData(morningDataSet, eveningDataSet, extraDataSet);

        barData.setBarWidth(1f / 3f);

        //barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        barChart.getXAxis().setCenterAxisLabels(true);
        //barChart.getXAxis().setAvoidFirstLastClipping(true);

        barChart.setData(barData);
        barChart.groupBars(0f, 0f, 0f);

        //barChart.setVisibleXRange(0f, 8f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(dayCount);
        barChart.setPinchZoom(true);

        barChart.setScaleYEnabled(false);
        barChart.setScaleXEnabled(true);

        barChart.getXAxis().setValueFormatter(new MyValueFormatter(dates, (int)dayCount ));
        barChart.getDescription().setText(getString(R.string.PeakAirflow));
        barChart.setDrawValueAboveBar(false);
        barChart.invalidate();
    }
}