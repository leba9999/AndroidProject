package com.example.pefproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OldRecordActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA = "OldRecords";
    private String recordType;

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

    private Date startDay;
    private Date endDay;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_record);

        startDateText = findViewById(R.id.startDateTextViewOld);
        endDateText = findViewById(R.id.endDateTextViewOld);

        dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());

        calendar = Calendar.getInstance();
        startDay = new Date();
        endDay = new Date();

        calendar.add(Calendar.DATE, -10);
        startDay = calendar.getTime();
        calendar = Calendar.getInstance();
        endDay = calendar.getTime();

        day_1 = startDay.getTime();
        day_2 = endDay.getTime();

        startDateText.setText(dateFormat.format(startDay.getTime()));
        endDateText.setText(dateFormat.format(endDay.getTime()));

        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;

        this.year = 0;
        this.month = 0;
        this.day = 0;

        startDateText.setOnClickListener(v -> {
            showDatePickerDialog(startDay);
            dayTextView = true;
        });
        endDateText.setOnClickListener(v -> {
            showDatePickerDialog(endDay);
            dayTextView = false;
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        if (dayTextView){
            calendar.set(this.year, this.month , this.day);
            startDay = calendar.getTime();
            startDateText.setText(dateFormat.format(startDay.getTime()));
            day_1 = calendar.getTimeInMillis();
        } else{
            calendar.set(this.year, this.month , this.day);
            endDay = calendar.getTime();
            endDateText.setText(dateFormat.format(endDay.getTime()));
            day_2 = calendar.getTimeInMillis();
        }
        dayCount = ((day_2 - day_1) / (24 * 60 * 60 * 1000)) + 1;
    }
    public void showDatePickerDialog(Date date){
        calendar.setTimeInMillis(date.getTime());
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void updateUI() {
        ListView listView = findViewById(R.id.listViewDates);

        ArrayList<Record> records = Singleton.getInstance().getRecording();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        calendar.setTime(startDay);

        dates.add(dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){

            calendar.add(Calendar.DATE, 1);

            if (!dates.contains(dateFormat.format(calendar.getTime()))) {
                dates.add(dateFormat.format(calendar.getTime()));
            }
        }


        //Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < dayCount; i++) {
            for (int j = 0; j < records.size(); j++) {
                if (dateFormat.format(records.get(j).getDate()).equals(dates.get((  (((int)dayCount - 1) - i))))) {
                    switch (records.get(j).getType()) {
                        case Record.AM:
                            recordType = getString(R.string.Morning);
                            break;
                        case Record.PM:
                            recordType = getString(R.string.Evening);
                            break;
                        case Record.EXTRA:
                            recordType = getString(R.string.Extra);
                            break;
                    }
                    dateList.add(dateFormat.format(records.get(i).getDate()) + "\t \t" + recordType + "\t" +
                            +records.get(i).getPeakNormalAirflow() + "\t"
                            + records.get(i).getPeakMedicineAirflow());
                }
            }
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateList));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent nextActivity = new Intent(OldRecordActivity.this, NewRecordActivity.class);
            nextActivity.putExtra(EXTRA, position);
            startActivity(nextActivity);

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