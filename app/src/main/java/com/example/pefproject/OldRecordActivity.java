package com.example.pefproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Luokka hallitsee Vanha Merkintä aktiviteettiä. (OldRecordActivity).
 * @author Peetu Salonen
 * @version 0.01 07.03.2021
 */
public class OldRecordActivity extends AppCompatActivity {

    /**
     * Intentin avain. Avaimen taakse laitetaan ListView:istä valitun Merkinnän paikka (indeksi).
     */
    public static final String EXTRA = "OldRecords";
    private String recordType;

    private TextView startDateText;
    private TextView endDateText;

    private boolean dayTextView;
    private long dayCount;

    private SimpleDateFormat dateFormat;

    private Date startDay;
    private Date endDay;
    private Calendar calendar;
    /**
     * Kutsutaan kun aktiviteetti luodaan. Asetetaan kaikille luokan muuttujille arvot.
     * Asetetaan setOnClickListener funktiot TextView:ille ja setOnDateSetListener DatePickerDialog:ille.
     * @param savedInstanceState sisältää aktiviteetin tallennetun instancen
     */
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

        startDateText.setText(dateFormat.format(startDay.getTime()));
        endDateText.setText(dateFormat.format(endDay.getTime()));

        dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        startDateText.setOnClickListener(v -> {
            calendar.setTime(startDay);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            dayTextView = true;
        });
        endDateText.setOnClickListener(v -> {
            calendar.setTime(endDay);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            dayTextView = false;
        });
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            if (dayTextView){
                startDay = calendar.getTime();
                startDateText.setText(dateFormat.format(startDay.getTime()));
            } else {
                endDay = calendar.getTime();
                endDateText.setText(dateFormat.format(endDay.getTime()));
            }
            dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;
            updateUI();
        });
    }

    /**
     * Hakee merkintöjen listan.
     * Asettaa Layouttiin haetun listan sekä oikean päivämäärän.
     * Aloittaa NewRecord aktiviteetin ja tuo valitun päivän datan muokkaamista varten.
     */
    public void updateUI() {
        ListView listView = findViewById(R.id.listViewDates);

        ArrayList<Record> records = Singleton.getInstance().getRecording();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        calendar.setTime(startDay);

        dates.add(dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){
            calendar.add(Calendar.DATE, 1);
            if (!dates.contains(dateFormat.format(calendar.getTime()))) {
                dates.add(dateFormat.format(calendar.getTime()));
            }
        }

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
                    data.add(dateFormat.format(records.get(j).getDate()) + "\t \t" + recordType + "\t" +
                            +records.get(j).getPeakNormalAirflow() + "\t"
                            + records.get(j).getPeakMedicineAirflow());
                    dateList.add(dateFormat.format(records.get(j).getDate()));
                }
            }
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent nextActivity = new Intent(OldRecordActivity.this, NewRecordActivity.class);
            for (int j = 0; j < records.size(); j++) {
                if (dateList.get(position).equals(dateFormat.format(records.get(j).getDate()))){
                    Log.i("APP_OLD", " " + j + " " + dateFormat.format(records.get(j).getDate()));
                    nextActivity.putExtra(EXTRA, j);
                }
            }
            startActivity(nextActivity);
        });
    }

    /**
     * Lataa sharedPreferenseistä Record classin ArrayListin.
     * Suorittaa metodin updateUI().
     */
    @Override
    protected void onResume() {
        Singleton.getInstance().loadData(this);
        updateUI();
        super.onResume();
    }

    /**
     * Tallentaa sharedPreferenseihin ArrayListin joka sisältää Record classin.
     */
    @Override
    protected void onPause() {
        Singleton.getInstance().saveData(this);
        super.onPause();
    }
}