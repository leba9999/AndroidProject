package com.example.pefproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

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

public class OldRecordActivity extends AppCompatActivity {

    public static final String EXTRA = "OldRecords";
    private String recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_record);
        ListView listView = findViewById(R.id.listViewDates);

        ArrayList<Record> records = Singleton.getInstance().getRecording();

        List<String> dateList = new ArrayList<String>();
        //Calendar calendar = Calendar.getInstance();
        for (int i=0; i<records.size(); i++) {
          //  calendar.add(Calendar.DATE, 1);
            SimpleDateFormat curFormater = new SimpleDateFormat(Singleton.getInstance().getDateFormat());

            //String s = " ";
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
            dateList.add(curFormater.format(records.get(i).getDate()) + "\t \t" + recordType + "\t" +
                    + records.get(i).getPeakNormalAirflow() + "\t"
                    + records.get(i).getPeakMedicineAirflow());
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent(OldRecordActivity.this, NewRecordActivity.class);
                nextActivity.putExtra(EXTRA, position);
                startActivity(nextActivity);

            }
        });
    }
}