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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

    private String normalFirstLetter;
    private String medicineFirstLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(logTag, " Create: Start");
        //Singleton.getInstance().loadData(this);

        setTestRecords();

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

        normalFirstLetter = getString(R.string.Normal).substring(0, getString(R.string.Normal).length()-(getString(R.string.Normal).length() - 1));
        medicineFirstLetter = getString(R.string.Medicine).substring(0, getString(R.string.Medicine).length()-(getString(R.string.Medicine).length() - 1));

        morningTextView.setText(getString(R.string.Morning) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");
        eveningTextView.setText(getString(R.string.Evening) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");
        extraTextView.setText(getString(R.string.Extra) +":\n"+ normalFirstLetter + ":---\n" + medicineFirstLetter + ":---");
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
    private void setUpChart(){
        BarChart barChart = findViewById(R.id.barView);
        // Testi arvot
        ArrayList<BarEntry> airflow = new ArrayList<>();
        ArrayList<BarEntry> airflowNight = new ArrayList<>();
        ArrayList<BarEntry> airflowExrta = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            airflow.add(new BarEntry(i, 0));
            airflowNight.add(new BarEntry(i, 0));
            airflowExrta.add(new BarEntry(i, 0));
            float [] values = {
                    300f + 10*i,
                    200f + 10*i
            };
            float [] values_2 = {
                    320f + 10*i,
                    250f + 10*i
            };
            float [] values_3 = {
                    400f + 10*i
            };
            airflow.get(i).setVals(values);
            airflowNight.get(i).setVals(values_2);
            airflowExrta.get(i).setVals(values_3);


        }

        int [] color = {
                ContextCompat.getColor(this, R.color.white_230) ,
                ContextCompat.getColor(this, R.color.light_blue)
        };
        int [] nightColor = {
                ContextCompat.getColor(this, R.color.grey) ,
                ContextCompat.getColor(this, R.color.dark_blue)
        };
        String [] colorLabels = {
                getString(R.string.Normal) ,
                getString(R.string.Medicine)
        };
        BarDataSet barDataSet = new BarDataSet(airflow, "");
        barDataSet.setColors(color);
        barDataSet.setStackLabels(colorLabels);
        //barDataSet.setColor(ContextCompat.getColor(this, R.color.white_230));


        BarDataSet barDataSet_2 = new BarDataSet(airflowNight, "");
        barDataSet_2.setColors(nightColor);
        barDataSet_2.setStackLabels(colorLabels);

        BarDataSet barDataSet_3 = new BarDataSet(airflowExrta, "Ylimär.");
        barDataSet_3.setColor(ContextCompat.getColor(this, R.color.yellow));

        BarData data = new BarData(barDataSet, barDataSet_2, barDataSet_3);
        float groupSpace = 0f;
        float barSpace = 0;
        float barWidth = 0.5f;
        barChart.setData(data);
        data.setBarWidth(barChart.getXAxis().getGridLineWidth() / 3);

        barChart.groupBars(0, groupSpace, barSpace);
        barChart.setFitBars(true);
        //barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setAvoidFirstLastClipping(true);
        barChart.setVisibleXRange(0f, 7.4f);
        barChart.setScaleYEnabled(false);
        //barChart.getXAxis().setAxisMaximum(9);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setValueFormatter(new MyValueFormatter());
        //barChart.getXAxis().setGranularity(1f);

        /*
        ArrayList<BarEntry> airflow = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            airflow.add(new BarEntry(i, 0));
            float [] values = {
                    300f + 10*i
            };
            airflow.get(i).setVals(values);
        }
        ArrayList<BarEntry> airflow2 = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            airflow2.add(new BarEntry(i, 0));
            float [] values = {
                    200f + 10*i
            };
            airflow2.get(i).setVals(values);
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
                getString(R.string.Normal) ,
                getString(R.string.Medicine)
        };
        barDataSet.setColors(color);
        BarDataSet barDataSet_2 = new BarDataSet(airflow2, "");
        barDataSet_2.setColor(ContextCompat.getColor(this, R.color.dark_blue));
        barDataSet_2.setValueTextColor(Color.BLACK);
        barDataSet_2.setValueTextSize(16.f);
        BarData barData = new BarData(barDataSet, barDataSet_2);
        barChart.getXAxis().setValueFormatter(new MyValueFormatter());
        //barChart.getXAxis().setCenterAxisLabels(true);
        //barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(false);
        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.43f;
        barData.setBarWidth(barWidth);
        //barChart.groupBars(1, groupSpace, barSpace);
        barChart.setData(barData);
        barChart.getDescription().setText("Peak AirFlow");
        barChart.getDescription().setYOffset(-10f);
        */
    }
    public void buttonPressed (View view) {
        //Get widgets view id
        Intent intent;
        switch (view.getId()) {
            // Buttons id:
            case R.id.NewRecordButton:
                intent = new Intent(this, NewRecordActivity.class);
                intent.putExtra(OldRecordActivity.EXTRA, -1);
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
    private void setTestRecords(){
        for (int i = 0; i < 7; i++) {
            Singleton.getInstance().addRecord(new Record());
            Singleton.getInstance().getRecording().get(i).addNormalAirflow(130 + 10*i);
            Singleton.getInstance().getRecording().get(i).addNormalAirflow(145+ 10*i);
            Singleton.getInstance().getRecording().get(i).addNormalAirflow(140+ 10*i);
            Singleton.getInstance().getRecording().get(i).addMedicineAirflow(222+ 10*i);
            Singleton.getInstance().getRecording().get(i).addMedicineAirflow(212+ 10*i);
            Singleton.getInstance().getRecording().get(i).addMedicineAirflow(232+ 10*i);

            Calendar calendar = Calendar.getInstance();
            calendar.set(2021, 2, i+1);
            Singleton.getInstance().getRecording().get(i).setDate(calendar.getTime());
        }
        for (int i = 0; i < Singleton.getInstance().getRecording().size(); i++){
            Log.i(logTag, "Record ["+ i +"] : " + Singleton.getInstance().getRecording().get(i).toString());
        }
        Singleton.getInstance().getRecording().get(0).setType(0);
    }
    @Override
    protected void onPause() {
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