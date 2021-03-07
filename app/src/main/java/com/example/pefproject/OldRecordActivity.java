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

        // Haetaan kalenteri ja luodaan uudet päivä oliot
        calendar = Calendar.getInstance();
        startDay = new Date();
        endDay = new Date();

        // vähennetään kymmenen päivää aloituspäivästä jotta saadaan näkyviin tästä päivästä 10 päivää taaksepäin merkintöjä
        calendar.add(Calendar.DATE, -10);
        startDay = calendar.getTime();
        calendar = Calendar.getInstance();
        endDay = calendar.getTime();

        // Asetetaan päivämäärät textView:n näkyviin.
        startDateText.setText(dateFormat.format(startDay.getTime()));
        endDateText.setText(dateFormat.format(endDay.getTime()));

        // Lasketaan aloitus ja lopetus päivien välillä olevien päivien määrät.
        dayCount = ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        // Asetetaan OnClieckListener starDateTextViewille jossa asetetaan kalenterille ajaksi startDate koska Date luokasta ei enään saa
        // vuotta, kuukautta ja päivää ulos. Kalenterin kautta voidaan antaa datePickerDialog:lle päivämäärä jossa se on "oletuksena" kun avataan dialog
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
        // Asetetaan setOnDateSetListener datePickerDialog:lle jossa katsotaan kumpaa textView:ä(aloitus- ja lopetuspäivä) muokataan dayTextView muutujalla
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
            // päivitetään listView
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
        // Väliaikaset listat
        ArrayList<Record> records = Singleton.getInstance().getRecording();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();

        // Annetaan kalenterille aloitus päivä johon sitten lisätään 1 päivä aina lopetus päivään asti
        calendar.setTime(startDay);
        dates.add(dateFormat.format(startDay));
        for (int i = 1; i < dayCount; i++){
            // Nostetaan kalenterin päivämäärää yhdellä
            calendar.add(Calendar.DATE, 1);
            // tarkastetaan löytyykö kalenterin päivämäärä dates ArrayListasta jos ei lisätään se. Tarkoitus on 100% varmistaa ettei tule kopioita päivistä
            if (!dates.contains(dateFormat.format(calendar.getTime()))) {
                dates.add(dateFormat.format(calendar.getTime()));
            }
        }

        String normalFirstLetter = getString(R.string.Normal).substring(0, getString(R.string.Normal).length()-(getString(R.string.Normal).length() - 1));
        String medicineFirstLetter = getString(R.string.Medicine).substring(0, getString(R.string.Medicine).length()-(getString(R.string.Medicine).length() - 1));
        // Käydään kaikki päivät läpi jotka halutaan näyttää listview:sä
        for (int i = 0; i < dayCount; i++) {
            // Käydään läpi kaikki merkinnät
            for (int j = 0; j < records.size(); j++) {
                String ListViewText = "[" + dateFormat.format(records.get(j).getDate()) + "] " + records.get(j).getTypeString(this)  + ":  "
                        + normalFirstLetter + ":" + records.get(j).getPeakNormalAirflow() + "   "
                        + medicineFirstLetter + ":" + records.get(j).getPeakMedicineAirflow();
                // Verrataan onko halutulle päivämäärälle merkintää.
                // records.get(j).getDate() - merkinnän pävimäärä
                // dates.get(((int)dayCount - 1) - i) haluttu päivämäärä.
                // Merkintä halutaan lisätä päinvastaisessa järjestyksessä sen takia joudutaan laskemaan dates.size() - 1) - i
                // Muuten kyllä pelkka i riittäisi
                if (dateFormat.format(records.get(j).getDate()).equals(dates.get((dates.size()- 1) - i))) {
                    data.add(ListViewText);
                }
                // lisätään vain kerran kaikki recordien arvot datelistiin jotta sitä voidaan myöhemmin verrata data listiin
                if (i < 1){
                    dateList.add(ListViewText);
                }
            }
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent nextActivity = new Intent(OldRecordActivity.this, NewRecordActivity.class);
            // Ongelmana on että position on vain listviewin paikka arvo eli se ei kerro varsinaisesti merkinnän indeksiä joka pitää antaa NewRecodActivitylle
            // joten joudutaan vertaamaan data joka on listviewissä kaikkiin merkintöihin ja sitten siirtä kyseisen merkinnän indeksi nextActivityn Extraksi
            for (int j = 0; j < records.size(); j++) {
                if (data.get(position).equals(dateList.get(j))){
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