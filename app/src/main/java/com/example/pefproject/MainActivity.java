package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_MainActivity.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Record record = new Record();

        BarChart barChart = findViewById(R.id.barView);

        ArrayList<BarEntry> airflow = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            airflow.add(new BarEntry(i, 0));
            float [] values = {
                    200f + 10*i, 300f + 10*i
            };
            airflow.get(i).setVals(values);
        }

        Log.i(logTag, "Color.BLACK: " + Color.BLACK + " colors.xml: " + R.color.black);
        Log.i(logTag, "Color.BLACK: " + Color.BLACK + " ContextCompat  Black: "  +  ContextCompat.getColor(this, R.color.black) );

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