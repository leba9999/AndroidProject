package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Luokka hallitsee Uusi merkintä aktiviteettiä.
 * @author Janne Lähteenmäki
 * @version 0.01 5.3.2021
 */
public class NewRecordActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_NewRecordActivity.java";

    private TextView dateTextView;
    private TextView medlesTextView;
    private TextView medTextView;
    private RadioGroup radioGroupTime;
    private Record record;
    private Calendar calendar;
    private EditText firstNumberNormal;
    private EditText secondNumberNormal;
    private EditText thirdNumberNormal;
    private EditText firstNumberMed;
    private EditText secondNumberMed;
    private EditText thirdNumberMed;
    private EditText commentText;
    private String recordType;
    private RadioButton rbMorn, rbEve, rbExtra;
    private Date date;
    private Intent nextActivity;
    private Button buttonSave;
    private Bundle b;
    private int i;
    private ArrayList<Record> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        Singleton.getInstance().loadData(this);

        dateTextView = findViewById(R.id.textViewDate);

        b = getIntent().getExtras();
        i = b.getInt(OldRecordActivity.EXTRA, -1);
        records = Singleton.getInstance().getRecording();
        medlesTextView = findViewById(R.id.textViewMedles);
        medTextView = findViewById(R.id.textViewMed);
        radioGroupTime = findViewById(R.id.radioGroupTime);
        commentText = findViewById(R.id.editTextComment);
        record = new Record();
        rbMorn = findViewById(R.id.radioButtonMorn);
        rbEve = findViewById(R.id.radioButtonEve);
        rbExtra = findViewById(R.id.radioButtonOver);
        buttonSave = findViewById(R.id.buttonSave);

        firstNumberNormal = findViewById(R.id.editTextFirstNumberNormal);
        secondNumberNormal = findViewById(R.id.editTextSecondNumberNormal);
        thirdNumberNormal = findViewById(R.id.editTextThirdNumberNormal);
        firstNumberMed = findViewById(R.id.editTextFirstNumberMed);
        secondNumberMed = findViewById(R.id.editTextSecondNumberMed);
        thirdNumberMed = findViewById(R.id.editTextThirdNumberMed);

        firstNumberNormal.addTextChangedListener(saveTextWatcher);
        secondNumberNormal.addTextChangedListener(saveTextWatcher);
        thirdNumberNormal.addTextChangedListener(saveTextWatcher);
        firstNumberMed.addTextChangedListener(saveTextWatcher);
        secondNumberMed.addTextChangedListener(saveTextWatcher);
        thirdNumberMed.addTextChangedListener(saveTextWatcher);



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        if (i == -1) {
            //Date date = calendar.getTime();
            date = record.getDate();
            dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(date));
        } else {
            date = records.get(i).getDate();
            dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(date));
            //for(int index = 0; index < records.get(i).getNormalAirflowList().size(); index++) {
            firstNumberNormal.setText(records.get(i).getNormalAirflowList().get(0).toString());
            secondNumberNormal.setText(records.get(i).getNormalAirflowList().get(1).toString());
            thirdNumberNormal.setText(records.get(i).getNormalAirflowList().get(2).toString());
            firstNumberMed.setText(records.get(i).getMedicineAirflowList().get(0).toString());
            secondNumberMed.setText(records.get(i).getMedicineAirflowList().get(1).toString());
            thirdNumberMed.setText(records.get(i).getMedicineAirflowList().get(2).toString());
            commentText.setText(records.get(i).getComment());

            switch (records.get(i).getType()) {
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
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        dateTextView.setOnClickListener(v -> {
            calendar.setTime(date);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            date = calendar.getTime();
            dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(date));
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("dsd", "ascasdasdasfasfaf");
                if (i < 0) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    nextActivity = new Intent(this, OldRecordActivity.class);
                    //nextActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //nextActivity.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
                    nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
                    startActivity(nextActivity);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * TextWatcher metodi seuraa onko puhallusarvo paikoille kirjoitettu merkkejä. (Normal ja Med editTextViewit).
     * Mikäli yksikin paikka on tyhjänä, tallentaminen ei onnistu.
     * Kun kaikki paikat ovat täytetty vähintään yhdellä merkillä, tallennus nappula muuttuu enable muotoon ja tallennus nappulaa voi painaa.
     */
    private TextWatcher saveTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstNormalInput = firstNumberNormal.getText().toString();
            String secondNormalInput = secondNumberNormal.getText().toString();
            String thirdNormalInput = thirdNumberNormal.getText().toString();
            String firstMedInput = firstNumberMed.getText().toString();
            String secondMedInput = secondNumberMed.getText().toString();
            String thirdMedInput = thirdNumberMed.getText().toString();

            buttonSave.setEnabled(!firstNormalInput.isEmpty() && !secondNormalInput.isEmpty() &&
                    !thirdNormalInput.isEmpty() && !firstMedInput.isEmpty() &&
                    !secondMedInput.isEmpty() && !thirdMedInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * Kun tallennusnappulaa painetaan suoritetaan metodi saveRecords.
     * Tallennetaan saadut tiedot sharedpreferenceihin.
     * Vaihdetaan activityä activityyn, josta on siirrytty nykyiseen NewRecordActivityyn.
     * @param view Tallenna nappulan view.
     */
    public void onSaveButtonClicked(View view) {
        saveRecords();
        Singleton.getInstance().saveData(this);

        Bundle b = getIntent().getExtras();
        int x = b.getInt(OldRecordActivity.EXTRA, -1);

        if (x == -1) {
            nextActivity = new Intent(this, MainActivity.class);

            //nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //nextActivity.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        } else {
            nextActivity = new Intent(this, OldRecordActivity.class);
            //nextActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //nextActivity.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
            nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        }
        startActivity(nextActivity);
    }

    /**
     * Asettaa listan tyypin riippuen mikä radiobutton on painettuna. (Aamu/ilta/ylimääräinen).
     * Lisää oikeaan listaan luvut jotka ovat syötetty puhallusarvo paikoille. (Normal ja Med editTextViewit).
     */
    public void saveRecords() {

        int type;

        switch (radioGroupTime.getCheckedRadioButtonId()) {
            case R.id.radioButtonEve:
                type = Record.PM;
                break;
            case R.id.radioButtonMorn:
                type = Record.AM;
                break;
            case R.id.radioButtonOver:
                type = Record.EXTRA;
                break;
            default:
                type = -1;
                break;
        }

        if (i == -1) {
            record.addNormalAirflow(Integer.parseInt(firstNumberNormal.getText().toString()));
            record.addNormalAirflow(Integer.parseInt(secondNumberNormal.getText().toString()));
            record.addNormalAirflow(Integer.parseInt(thirdNumberNormal.getText().toString()));
            record.addMedicineAirflow(Integer.parseInt(firstNumberMed.getText().toString()));
            record.addMedicineAirflow(Integer.parseInt(secondNumberMed.getText().toString()));
            record.addMedicineAirflow(Integer.parseInt(thirdNumberMed.getText().toString()));
            record.setComment(commentText.getText().toString());
            record.setDate(date);
            record.setType(type);
            Singleton.getInstance().addRecord(record);
        } else {
            records.get(i).getNormalAirflowList().set(0, Integer.parseInt(firstNumberNormal.getText().toString()));
            records.get(i).getNormalAirflowList().set(1, Integer.parseInt(secondNumberNormal.getText().toString()));
            records.get(i).getNormalAirflowList().set(2, Integer.parseInt(thirdNumberNormal.getText().toString()));
            records.get(i).getMedicineAirflowList().set(0, Integer.parseInt(firstNumberMed.getText().toString()));
            records.get(i).getMedicineAirflowList().set(1, Integer.parseInt(secondNumberMed.getText().toString()));
            records.get(i).getMedicineAirflowList().set(2, Integer.parseInt(thirdNumberMed.getText().toString()));
            records.get(i).setComment(commentText.getText().toString());
            records.get(i).setType(type);
            records.get(i).setDate(date);

        }
    }

}