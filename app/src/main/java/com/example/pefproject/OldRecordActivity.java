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



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OldRecordActivity extends AppCompatActivity {

    public static final String EXTRA = "OldRecords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_record);
        ListView listView = findViewById(R.id.listViewDates);

        List<String> dateList = new ArrayList<String>();
        //Calendar calendar = Calendar.getInstance();
        for (int i=0; i<Singleton.getInstance().getRecording().size(); i++) {
          //  calendar.add(Calendar.DATE, 1);
            SimpleDateFormat curFormater = new SimpleDateFormat(Singleton.getInstance().getDateFormat());

            String s = " ";
            switch (Singleton.getInstance().getRecording().get(i).getType()) {
                case Record.AM:
                    s = "Aamu";
                    break;
                case Record.PM:
                    s = "Ilta";
                    break;
                case Record.EXTRA:
                    s = "Extra";
                    break;
            }
            dateList.add(curFormater.format(Singleton.getInstance().getRecording().get(i).getDate()) + "         " + s + "    " +
                    + Singleton.getInstance().getRecording().get(i).getPeakNormalAirflow() + "    "
                    + Singleton.getInstance().getRecording().get(i).getPeakMedicineAirflow());
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