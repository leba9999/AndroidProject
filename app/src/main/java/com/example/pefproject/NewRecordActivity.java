package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewRecordActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_NewRecordActivity.java";
    private TextView dateTextView;
    private TextView medlesTextView;
    private TextView medTextView;
    private RadioGroup radioGroupTime;
    private Record record;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        Singleton.getInstance().loadData(this);

        dateTextView = findViewById(R.id.textViewDate);
        medlesTextView = findViewById(R.id.textViewMedles);
        medTextView = findViewById(R.id.textViewMed);
        radioGroupTime = findViewById(R.id.radioGroupTime);
        record = new Record();

        calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        dateTextView.setText(getString(R.string.textViewDate) + ": " + dateFormat.format(date));
    }

    public void onSaveButtonClicked(View view) {
        saveRecords();
        Singleton.getInstance().saveData(this);
    }

    public void saveRecords() {
        switch (radioGroupTime.getCheckedRadioButtonId()) {
            case R.id.radioButtonEve:
                record.setType(Record.PM);
                break;
            case R.id.radioButtonMorn:
                record.setType(Record.AM);
                break;
            case R.id.radioButtonOver:
                record.setType(Record.EXTRA);
                break;
            default:
                break;
        }
        EditText firstNumberNormal = (EditText) findViewById(R.id.editTextFirstNumberNormal);
        EditText secondNumberNormal = (EditText) findViewById(R.id.editTextSecondNumberNormal);
        EditText thirdNumberNormal = (EditText) findViewById(R.id.editTextThirdNumberNormal);
        EditText firstNumberMed = (EditText) findViewById(R.id.editTextFirstNumberMed);
        EditText secondNumberMed = (EditText) findViewById(R.id.editTextSecondNumberMed);
        EditText thirdNumberMed = (EditText) findViewById(R.id.editTextThirdNumberMed);
        EditText comment = (EditText) findViewById(R.id.editTextComment);

        record.addNormalAirflow(Integer.parseInt(firstNumberNormal.getText().toString()));
        record.addNormalAirflow(Integer.parseInt(secondNumberNormal.getText().toString()));
        record.addNormalAirflow(Integer.parseInt(thirdNumberNormal.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(firstNumberMed.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(secondNumberMed.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(thirdNumberMed.getText().toString()));
        record.setComment(comment.getText().toString());
        record.setDate(calendar.getTime());
        Singleton.getInstance().addRecord(record);
    }

}