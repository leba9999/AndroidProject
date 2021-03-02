package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
public class MainActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_MainActivity.java";
    private TextView morningTextView;
    private TextView eveningTextView;
    private TextView extraTextView;
    private TextView recordTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(logTag, " Create: Start");
        Singleton.getInstance().loadData(this);

        for (int i = 0; i < Singleton.getInstance().getRecording().size(); i++){
            Log.i(logTag, "Record ["+ i +"] : " + Singleton.getInstance().getRecording().get(i).toString());
        }
        setUpChart();
        morningTextView = findViewById(R.id.morningTextView);
        eveningTextView = findViewById(R.id.eveningTextView);
        extraTextView = findViewById(R.id.extraTextView);
        recordTextView = findViewById(R.id.recordTextView);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        recordTextView.setText(getString(R.string.Record) + ": " + dateFormat.format(date));
        loadDayRecord();
        morningTextView.setText(getString(R.string.Morning) +":\n"+ getString(R.string.Normal) + ":---\n" + getString(R.string.Medicine) + ":---");
        eveningTextView.setText(getString(R.string.Evening) +":\n"+ getString(R.string.Normal) + ":---\n" + getString(R.string.Medicine) + ":---");
        extraTextView.setText(getString(R.string.Extra) +":\n"+ getString(R.string.Normal) + ":---\n" + getString(R.string.Medicine) + ":---");
        Log.i(logTag, " Create: Ready");
    }
    private void loadDayRecord(){
        ArrayList<Record> records = Singleton.getInstance().getRecording();
        if (records.isEmpty()){
            return;
        }
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        for (int i = 0; i < records.size(); i++){
            SimpleDateFormat dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());
            if (dateFormat.format(records.get(i).getDate()).equals(dateFormat.format(calendar.getTime()))){
                Log.i(logTag, " loadDayRecord");
                if (records.get(i).getType() == records.get(i).PM){
                    eveningTextView.setText(getString(R.string.Evening) +":\n"
                            + getString(R.string.Normal) + ": " + records.get(i).getPeakNormalAirflow() +"\n"
                            + getString(R.string.Medicine) + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == records.get(i).AM) {
                    morningTextView.setText(getString(R.string.Morning) +":\n"
                            + getString(R.string.Normal) + ": " + records.get(i).getPeakNormalAirflow() + "\n"
                            + getString(R.string.Medicine) + ": " + records.get(i).getPeakMedicineAirflow());
                }else if (records.get(i).getType() == records.get(i).EXTRA) {
                    extraTextView.setText(getString(R.string.Extra) +":\n"
                            + getString(R.string.Normal) + ": " + records.get(i).getPeakNormalAirflow() + "\n"
                            + getString(R.string.Medicine) + ": " + records.get(i).getPeakMedicineAirflow());
                }
            }
        }
    }
    private void setUpChart(){
        BarChart barChart = findViewById(R.id.barView);
        // Testi arvot
        ArrayList<BarEntry> airflow = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            airflow.add(new BarEntry(i, 0));
            float [] values = {
                    200f + 10*i, 300f + 10*i
            };
            airflow.get(i).setVals(values);
        }

        BarDataSet barDataSet = new BarDataSet(airflow, "");
        Color.rgb(48, 174, 255);
        barDataSet.setColor(ContextCompat.getColor(this, R.color.light_blue));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16.f);

        int [] color = {
                ContextCompat.getColor(this, R.color.white_230) ,
                ContextCompat.getColor(this, R.color.light_blue)
        };
        String [] colorLabels = {
                "Normal" ,
                "Medicine"
        };
        barDataSet.setColors(color);
        barDataSet.setStackLabels(colorLabels);

        BarData barData = new BarData(barDataSet);
        barChart.getXAxis().setValueFormatter(new MyValueFormatter());
        //barChart.getXAxis().setCenterAxisLabels(true);
        //barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setData(barData);
        barChart.getDescription().setText("Peak AirFlow");
        barChart.getDescription().setYOffset(-10f);
    }
    public void buttonPressed (View view) {
        //Get widgets view id
        Intent intent;
        switch (view.getId()) {
            // Buttons id:
            case R.id.NewRecordButton:
                intent = new Intent(this, NewRecordActivity.class);
                startActivity(intent);
                break;
            /*case R.id.OldRecordActivity:
                intent = new Intent(this, OldRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.barView:
                break;*/
            default:
                intent = new Intent(this, OldRecordActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onPause() {
        Singleton.getInstance().addRecord(new Record());
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(130);
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(145);
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addNormalAirflow(140);
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(222);
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(212);
        Singleton.getInstance().getRecording().get(Singleton.getInstance().getRecording().size() - 1).addMedicineAirflow(232);
        Singleton.getInstance().saveData(this);
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.i(logTag, "onResume");
        loadDayRecord();
        for (int i = 0; i < Singleton.getInstance().getRecording().size(); i++){
            Log.i(logTag, "Record ["+ i +"] : " + Singleton.getInstance().getRecording().get(i).toString());
        }
        super.onResume();
    }
}