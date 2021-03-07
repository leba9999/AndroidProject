package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Luokka hallitsee Uusi merkintä aktiviteettiä. (NewRecordActivity).
 * @author Janne Lähteenmäki
 * @version 0.01 05.03.2021
 */
public class NewRecordActivity extends AppCompatActivity {

    private TextView dateTextView;
    private RadioGroup radioGroupTime;
    private Record record;
    private Calendar calendar;

    ArrayList<EditText> editTexts;

    private EditText commentText;

    private RadioButton rbMorn, rbEve, rbExtra;
    private Intent nextActivity;
    private Button buttonSave;
    private Button buttonDel;

    private int index;

    /**
     * Kutsutaan kun aktiviteetti luodaan. Asetetaan kaikille luokan muuttujille arvot.
     * Asetetaan setOnClickListener funktiot TextView:ille ja setOnDateSetListener DatePickerDialog:ille.
     * Jos aktiviteetti on kutsuttu oldRecordActivityn kautta haetaan intentin avulla tietyn merkinnän arvot ja asetetaan ne paikoilleen
     * @param savedInstanceState sisältää aktiviteetin tallennetun instancen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        Singleton.getInstance().loadData(this);

        dateTextView = findViewById(R.id.textViewDate);

        index = getIntent().getIntExtra(OldRecordActivity.EXTRA, -1);

        radioGroupTime = findViewById(R.id.radioGroupTime);
        commentText = findViewById(R.id.editTextComment);
        rbMorn = findViewById(R.id.radioButtonMorn);
        rbEve = findViewById(R.id.radioButtonEve);
        rbExtra = findViewById(R.id.radioButtonOver);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDel = findViewById(R.id.buttonDel);

        editTexts = new ArrayList<>();
        editTexts.add(findViewById(R.id.editTextFirstNumberNormal));
        editTexts.add(findViewById(R.id.editTextSecondNumberNormal));
        editTexts.add(findViewById(R.id.editTextThirdNumberNormal));

        editTexts.add(findViewById(R.id.editTextFirstNumberMed));
        editTexts.add(findViewById(R.id.editTextSecondNumberMed));
        editTexts.add(findViewById(R.id.editTextThirdNumberMed));

        for (int i = 0; i < editTexts.size(); i++){
            editTexts.get(i).addTextChangedListener(saveTextWatcher);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(Singleton.getInstance().getDateFormat(), Locale.getDefault());
        calendar = Calendar.getInstance();

        if (index < 0) {
            record = new Record();
            buttonDel.setVisibility(View.INVISIBLE);
        } else {
            record = Singleton.getInstance().getRecording().get(index);
            setTitle(getString(R.string.titleNewRecordActivityName_EDIT));

            editTexts.get(0).setText(record.getNormalAirflowList().get(0).toString());
            editTexts.get(1).setText(record.getNormalAirflowList().get(1).toString());
            editTexts.get(2).setText(record.getNormalAirflowList().get(2).toString());
            editTexts.get(3).setText(record.getMedicineAirflowList().get(0).toString());
            editTexts.get(4).setText(record.getMedicineAirflowList().get(1).toString());
            editTexts.get(5).setText(record.getMedicineAirflowList().get(2).toString());
            commentText.setText(record.getComment());
        }
        dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(record.getDate()));
        switch (record.getType()) {
            case Record.AM:
                rbMorn.setChecked(true);
                break;
            case Record.PM:
                rbEve.setChecked(true);
                break;
            case Record.EXTRA:
                rbExtra.setChecked(true);
                break;
            default:
                radioGroupTime.clearCheck();
                break;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        dateTextView.setOnClickListener(v -> {
            calendar.setTime(record.getDate());
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            record.setDate(calendar.getTime());
            dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(calendar.getTime()));
        });
    }

    /**
     * Ylikirjoitetaan aktiviteetin onOptionsItemSelected omalla funktiolla. Jotta voidaan palata joko aktiviteetin parent aktiviteettiin tai haluttuun aktiviteettiin  (OldRecordActivity)
     * https://developer.android.com/reference/android/app/Activity#onOptionsItemSelected(android.view.MenuItem)
     * @param item MenuItem: Valikon kohta joka valittiin. Arvo ei voi olla null.
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                if (index < 0) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    nextActivity = new Intent(this, OldRecordActivity.class);
                    nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
                    startActivity(nextActivity);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * TextWatcher metodi seuraa onko puhallusarvo kentille kirjoitettu merkkejä. (Normal ja Med editTextViewit).
     * Mikäli yksikin kenttä on tyhjänä, tallentaminen ei onnistu.
     * Kun kaikki kentät ovat täytetty vähintään yhdellä merkillä, tallennus nappula muuttuu enable muotoon ja tallennus nappulaa voi painaa.
     */
    private TextWatcher saveTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean notEmpty = true;
            for (int i = 0; i < editTexts.size(); i++){
                if (editTexts.get(i).getText().toString().isEmpty()){
                    notEmpty = false;
                }
            }
            buttonSave.setEnabled(notEmpty);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * Kun poistanappulaa painetaan, poistetaan valittuna oleva merkintä.
     * Vaihdetaan aktiviteettiä takaisin vanhoihin merkintöihin.
     * @param view Poista nappulan view.
     */
    public void deleteButtonClicked(View view) {
        Singleton.getInstance().getRecording().remove(index);
        Singleton.getInstance().saveData(this);

        nextActivity = new Intent(this, OldRecordActivity.class);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        startActivity(nextActivity);
    }
    /**
     * Kun tallennusnappulaa painetaan suoritetaan metodi saveRecords.
     * Tallennetaan saadut tiedot sharedpreferenceihin.
     * Vaihdetaan aktiviteetistä toiseen aktiviteettiin, josta on siirrytty nykyiseen NewRecordActivityyn.
     * @param view Tallenna nappulan view.
     */
    public void onSaveButtonClicked(View view) {
        saveRecords();
        Singleton.getInstance().saveData(this);

        if (index < 0) {
            nextActivity = new Intent(this, MainActivity.class);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            nextActivity = new Intent(this, OldRecordActivity.class);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        }
        startActivity(nextActivity);
    }

    /**
     * Asettaa Merkinnän tyypin riippuen mikä radiobutton on painettuna. (Aamu/ilta/ylimääräinen).
     * Lisää uuden tai vaihtaa muokatun Merkinnän, jonka arvot on syötetty puhallusarvojen paikoille. (Normal ja Med editTextViewit).
     */
    public void saveRecords() {
        record.addNormalAirflow(Integer.parseInt(editTexts.get(0).getText().toString()));
        record.addNormalAirflow(Integer.parseInt(editTexts.get(1).getText().toString()));
        record.addNormalAirflow(Integer.parseInt(editTexts.get(2).getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(editTexts.get(3).getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(editTexts.get(4).getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(editTexts.get(5).getText().toString()));
        record.setComment(commentText.getText().toString());
        if (radioGroupTime.getCheckedRadioButtonId() == R.id.radioButtonEve) {
            record.setType(Record.PM);
        } else if (radioGroupTime.getCheckedRadioButtonId() == R.id.radioButtonMorn) {
            record.setType(Record.AM);
        } else if (radioGroupTime.getCheckedRadioButtonId() == R.id.radioButtonOver) {
            record.setType(Record.EXTRA);
        } else {
                record.setType(-1);
        }
        if (index < 0) {
            Singleton.getInstance().addRecord(record);
        } else {
            Singleton.getInstance().getRecording().set(index, record);
        }
    }
}