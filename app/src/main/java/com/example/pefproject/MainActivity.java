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
        for (int i = 0; i < 7; i++){
            airflow.add(new BarEntry(i, 0));
            float [] values = {
                    200f + 10*i, 300f + 10*i
            };
            airflow.get(i).setVals(values);
        }

        Log.i("MAIN_ACTIVITY", "Color.BLACK: " + Color.BLACK + " colors.xml: " + R.color.black);
        Log.i("MAIN_ACTIVITY", "Color.BLACK: " + Color.BLACK + " ContextCompat  Black: "  +  ContextCompat.getColor(this, R.color.black) );

        BarDataSet medicineBarDataSet = new BarDataSet(airflow, "");
        Color.rgb(48, 174, 255);
        medicineBarDataSet.setColor(ContextCompat.getColor(this, R.color.light_blue));
        medicineBarDataSet.setValueTextColor(Color.BLACK);
        medicineBarDataSet.setValueTextSize(16.f);
        int [] color = {
                ContextCompat.getColor(this, R.color.white_230) ,
                ContextCompat.getColor(this, R.color.light_blue)
        };
        String [] colorLabels = {
                "Normal" ,
                "Medicine"
        };
        medicineBarDataSet.setColors(color);
        medicineBarDataSet.setStackLabels(colorLabels);

        BarData barData = new BarData(medicineBarDataSet);
        barChart.getXAxis().setValueFormatter(new MyValueFormatter());
        //barChart.getXAxis().setGranularity(2f);

        barChart.setFitBars(true);
        barChart.getXAxis().getFormattedLabel(0);
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