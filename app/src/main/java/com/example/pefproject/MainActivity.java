package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Record record = new Record();

        BarChart barChart = findViewById(R.id.barView);

        ArrayList<BarEntry> airflow = new ArrayList<>();
        airflow.add(new BarEntry(0, 200));
        airflow.add(new BarEntry(1, 210));
        airflow.add(new BarEntry(2, 300));
        airflow.add(new BarEntry(3, 340));
        airflow.add(new BarEntry(3, 320));
        BarDataSet barDataSet = new BarDataSet(airflow, "PEF");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16.f);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Peak AirFlow");

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
            case R.id.OldRecordActivity:
                intent = new Intent(this, OldRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.barView:
                intent = new Intent(this, OldRecordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}