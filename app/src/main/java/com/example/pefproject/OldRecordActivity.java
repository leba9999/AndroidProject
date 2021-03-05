package com.example.pefproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class OldRecordActivity extends AppCompatActivity {

    public static final String EXTRA = "OldRecords";
    private String recordType;
    private ArrayList<Record> records;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_record);
        Singleton.getInstance().loadData(this);
        updateUI();
    }

    public void updateUI() {
        Singleton.getInstance().loadData(this);
        records = Singleton.getInstance().getRecording();
        listView = findViewById(R.id.listViewDates);
        List<String> dateList = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            SimpleDateFormat curFormater = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

            switch (records.get(i).getType()) {
                case Record.AM:
                    recordType = "Aamu";
                    break;
                case Record.PM:
                    recordType = "Ilta";
                    break;
                case Record.EXTRA:
                    recordType = "Extra";
                    break;
            }
            dateList.add(curFormater.format(records.get(i).getDate()) + " \t " +  " \t " + recordType + " \t " +
                    records.get(i).getPeakNormalAirflow() + "\t"
                    + records.get(i).getPeakMedicineAirflow());

        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent(OldRecordActivity.this, NewRecordActivity.class);
                nextActivity.putExtra(EXTRA, position);
                //nextActivity.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
                nextActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(nextActivity);

            }
        });



    }

    @Override
    protected void onStart() {
        Singleton.getInstance().loadData(this);
        updateUI();
        super.onStart();
    }

    @Override
    protected void onResume() {
        Singleton.getInstance().loadData(this);
        updateUI();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Singleton.getInstance().loadData(this);
        updateUI();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Singleton.getInstance().saveData(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Singleton.getInstance().saveData(this);
        super.onStop();
    }

}