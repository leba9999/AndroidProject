package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Record record = new Record();
        record.addNormalAirflow(500);
        record.addNormalAirflow(510);
        record.addNormalAirflow(600);
        record.addNormalAirflow(490);
        record.addMedicineAirflow(530);
        record.addMedicineAirflow(532);
        record.addMedicineAirflow(531);
        Log.i("MAIN_APP", "Peak Airflow => " + record.getPeakNormalAirflow());
        Log.i("MAIN_APP", "Peak Airflow => " + record.getPeakMedicineAirflow());
    }

    public void onNewRecordClicked (View view) {
        Intent intent = new Intent(this, NewRecordActivity.class);
        startActivity(intent);
    }

    public void onOldRecordClicked (View view) {
        Intent intent = new Intent(this, OldRecordActivity.class);
        startActivity(intent);
    }
}