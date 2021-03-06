package com.example.pefproject;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Luokka sisältää merkinnälle kaiken tarpeellisen datan. päivämäärn, kellonajan, vapaaehtoisen kommentin, puhallusten arvot ja merkinnän tyypin (ilta, aamu, ylimääräinen)
 * @author Leevi Koskinen
 * @version 0.01 05.03.2021
 */
public class Record {
    // Normaali puhalluksen arvolista
    private final ArrayList<Integer> normalAirflow;
    // Lääke puhalluksen arvolista
    private final ArrayList<Integer> medicineAirflow;

    // Käyttäjän vapaa muotoinen komentti
    private String comment;

    // Merkinnän päivämäärä ja kellonaika muuttuja
    private Date date;

    // Määrittelee merkinnän tyypin eli on aamu, ilta vai ylimääräinen merkintä
    // luokka kun luodaan tyyppi valitaan automaattisesti riippuen kellonajasta
    private int type;
    /**
     * Merkinnän tyyppi AM.
     * Arvo = 0
     */
    public static final int AM = 0;
    /**
     * Merkinnän tyyppi PM.
     * Arvo = 1
     */
    public static final int PM = 1;
    /**
     * Merkinnän tyyppi EXTRA.
     * Arvo = 2
     */
    public static final int EXTRA = 2;
    /**
     * Record constructor asettaa tämän hetkisen päivämäärän ja kellon ajan kalenterin avulla
     * Luo myöskin uudet tyhjät ArrayListit normal- ja medicineAirflow listoille
     */
    public Record() {
        // Luodaan uudet listat
        this.normalAirflow = new ArrayList<>();
        this.medicineAirflow = new ArrayList<>();
        // Haetaan java kalenteri ja sen kautta kellon aika
        Calendar calendar = Calendar.getInstance();
        // vertaillaan onko aamu vai ilta ja asetetaan merkinnän tyyppi sen mukaan
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            this.type = PM;
        } else {
            this.type = AM;
        }
        // Asetetaan merkinnän päivämäärä
        this.date = calendar.getTime();
    }

    /**
     * Lisää normalAirflow Arraylistiin uuden arvon joka annetaan parametrissä
     * @param AirflowValue Normaali puhalluksen arvo
     */
    public void addNormalAirflow(int AirflowValue) {
        this.normalAirflow.add(AirflowValue);
        if(normalAirflow.size() > 3) {
            this.normalAirflow.remove(this.normalAirflow.size() - 1);
        }
    }

    /**
     * Lisää medicineAirflow Arraylistiin uuden arvon joka annetaan parametrissä
     * @param AirflowValue Lääke puhalluksen arvo
     */
    public void addMedicineAirflow(int AirflowValue) {
        this.medicineAirflow.add(AirflowValue);
        if(this.medicineAirflow.size() > 3) {
            this.medicineAirflow.remove(this.medicineAirflow.size() - 1);
        }
    }

    /**
     * Asetetaan merkinnän vapaa ehtoinen kommentti
     * @param comment Vapaa muotoinen kommentti teksti
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Asetetaan päivämäärä tarvittaessa ja aika
     * @param date uusi arvo.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * haetaan normalAirflown ArrayList
     * palauttaa normalAirflow arraylistin
     * @return ArrayList<Integer> normalAirflow
     */
    public ArrayList<Integer> getNormalAirflowList() {
        return this.normalAirflow;
    }

    /**
     * haetaan medicineAirflow ArrayList
     * palauttaa medicineAirflow Arraylistin
     * @return ArrayList<Integer> medicineAirflow
     */
    public ArrayList<Integer> getMedicineAirflowList() {
        return this.medicineAirflow;
    }

    /**
     * haetaan huippu arvo normalAirflow ArrrayListasta
     * palauttaa huippu arvon
     * @return int airflowPeak
     */
    public int getPeakNormalAirflow(){
        int airflowPeak = 0;
        // Käydään läpi normaalien puhallusten arvot ja asetetaan aina suurin tempPeak muuttujaan
        for (Integer number : this.normalAirflow) {
            if(number >= airflowPeak){
                airflowPeak = number;
            }
        }
        return airflowPeak;
    }

    /**
     * haetaan huippu arvo medicineAirflow ArrrayListasta
     * palauttaa huippu arvon
     * @return int airflowPeak
     */
    public int getPeakMedicineAirflow(){
        int airflowPeak = 0;
        // Käydään läpi lääke puhallusten arvot ja asetetaan aina suurin tempPeak muuttujaan
        for (Integer number : this.medicineAirflow) {
            if(number >= airflowPeak){
                airflowPeak = number;
            }
        }
        return airflowPeak;
    }

    /**
     * Palauttaa merkinnän kommentin
     * @return String comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Haetaan merkinnän päivämäärä
     * palauttaa merkinnän päivämäärän
     * @return String date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Millainen merkintä kyseessä. Onko merkintä aamu, ilta vai extra
     * AM = 0, PM = 1, EXTRA = 2
     * @return int type
     */
    public int getType() {
        return this.type;
    }

    /**
     * Millainen merkintä kyseessä. Onko merkintä aamu, ilta vai extra String muodossa
     * @return String
     */
    public String getTypeString(Context context) {
        switch (this.type) {
            case Record.AM:
                return context.getString(R.string.Morning);
            case Record.PM:
                return context.getString(R.string.Evening);
            case Record.EXTRA:
                return context.getString(R.string.Extra);
        }
        return "No Type";
    }

    /**
     * Millainen merkintä kyseessä. Onko merkintä aamu, ilta vai extra
     * AM = 0, PM = 1, EXTRA = 2
     * @param type asettaa merkinnän tyypin (0, 1, 2)
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    @NonNull
    public String toString() {
        // palautetaan puhallusten huippu arvot ja formaatit
        return " Normal: " + getPeakNormalAirflow() + " Medicine: " + getPeakMedicineAirflow() + " AM/PM/E:" + getType();
    }
}