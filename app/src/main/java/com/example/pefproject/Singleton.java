package com.example.pefproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Singleton luokka sisältää Merkintöjen listan, aika- ja päiväformaatit
 * Singletonilla on tarkoitus helpottaa muiden luokkien pääsy merkkien tietoihin, lisäämään niitä ja muokkaamaan suhteellisen hallitusti
 * Luokka tallentaa ja lataa SharedPreferenceseistä merkkien tiedot
 * @author Leevi Koskinen
 * @author Peetu Salonen
 * @version 0.01 05.03.2021
 */

public class Singleton {
    private final String sharedName = "Records";
    private final String sharedKey = "Recordings";

    private static final Singleton instance = new Singleton();
    private ArrayList<Record> recording;

    private final String timeFormat;
    private final String dateFormat;

    /**
     * Singleton constructor luo uuden merkintöjen listan ja asettaa aika-, päiväformaatin
     */
    private Singleton () {
        recording = new ArrayList<>();
        timeFormat = "HH:mm:ss";
        dateFormat = "dd.MM.yyyy";
    }

    public static Singleton getInstance() {
        return instance;
    }

    /**
     * Lisätään uusi merkintä merkintöjen listaan parametrilla record
     * @param record asetetaan merkintä listaan
     */
    public void addRecord(Record record) {
        recording.add(record);
    }

    /**
     * Palauttaa päivämääräformaatin eli missä muodossa päivämäärä pitäisi näyttää. Esim: 03.02.2021 tai 02/03/2021 jne...
     * @return String dateformat
     */
    public String getDateFormat(){
        return this.dateFormat;
    }

    /**
     * Palauttaa aikaformaatin eli missä muodossa kellonaika pitäisi näyttää. Esim: 21:49 tai 9.49 jne...
     * @return String timeFormat
     */
    public String getTimeFormat(){
        return this.timeFormat;
    }

    /**
     * Palauttaa merkintöjen listan.
     * @return ArrayList<Record> recording
     */
    public ArrayList<Record> getRecording() {
        return recording;
    }
    /**
     * Tallentaa sharedPreferenseihin ArrayListin joka sisältää Record classin. Kannattaa laittaa onPause() ja onStop() funktioihin. Tallentaa sitten aina kun activity pysähtyy
     * @param context Activityn contexti mistä tallennus kutsutaan
     */
    public void saveData(Context context){
        // Luodaan sharedPreferences ja käytetään jonkun activityn contextia jotta voidaan tallentaa dataa
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, MODE_PRIVATE);
        // Luodaan sharedPreferences editori jolla voidaan muokata sharedPreferenssejä
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        // Luodaan Gson olio jolla pystytään tallentamaan olio luokkia ja listoja
        Gson gson = new Gson();
        // gson muuntaa recording ArrayListan string muotoon joka voidaan tallentaa
        String json = gson.toJson(recording);
        // prefsEditor asettaa avaimelle arvoksi jsonin
        prefsEditor.putString(sharedKey, json);
        // prefsEditor hyväksyy muutokset
        prefsEditor.apply();
    }

    /**
     * Lataa sharedPreferenseistä Record classin ArrayListin. Ainakin activityjen onCreate() funktiossa kannattaa olla preffien lataus
     * @param context Activityn contexti mistä lataus kutsutaan
     */
    public void loadData(Context context){
        // Luodaan sharedPreferences ja käytetään jonkun activityn contextia jotta voidaan ladata dataa
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, MODE_PRIVATE);
        // Luodaan Gson olio jolla pystyy lataamaan kokonaisia olio luokkia ja listoja
        Gson gson = new Gson();
        // Haetaan sharedPreferenceistä string avaimella sharedKey ja jos ei löydy stingiä niin palautetaan arvo null
        String json = sharedPreferences.getString(sharedKey, null);
        // Jos json stringi on null, ei jatketa pidemmälle koska sharedpreferenseissä ei ollut dataa
        if (json == null){
             return;
        }
        // Luodaan type olio joka pystyy ottamaan vastaan Arraylistin joka sisältää Record classin
        Type type = new TypeToken<ArrayList<Record>>() {}.getType();
        // Muunnetaan json string gson.fromJson funktiolla ArrayListiksi ja asetetaan se recording listaan
        recording = gson.fromJson(json, type);
    }
}
