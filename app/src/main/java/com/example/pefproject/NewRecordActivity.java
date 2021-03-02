package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

        Calendar calendar = Calendar.getInstance();
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
                record.addNormalAirflow(R.id.editTextFirstNumberNormal);
                record.addNormalAirflow(R.id.editTextSecondNumberNormal);
                record.addNormalAirflow(R.id.editTextThirdNumberNormal);
                record.addMedicineAirflow(R.id.editTextFirstNumberMed);
                record.addMedicineAirflow(R.id.editTextSecondNumberMed);
                record.addMedicineAirflow(R.id.editTextThirdNumberMed);
                record.setComment(Integer.toString(R.id.editTextComment));
                record.setDate(calendar.getTime());
                record.setType(1);
                break;
            case R.id.radioButtonMorn:
                record.addNormalAirflow(R.id.editTextFirstNumberNormal);
                record.addNormalAirflow(R.id.editTextSecondNumberNormal);
                record.addNormalAirflow(R.id.editTextThirdNumberNormal);
                record.addMedicineAirflow(R.id.editTextFirstNumberMed);
                record.addMedicineAirflow(R.id.editTextSecondNumberMed);
                record.addMedicineAirflow(R.id.editTextThirdNumberMed);
                record.setComment(Integer.toString(R.id.editTextComment));
                record.setDate(calendar.getTime());
                record.setType(0);
                break;
            case R.id.radioButtonOver:
                record.addNormalAirflow(R.id.editTextFirstNumberNormal);
                record.addNormalAirflow(R.id.editTextSecondNumberNormal);
                record.addNormalAirflow(R.id.editTextThirdNumberNormal);
                record.addMedicineAirflow(R.id.editTextFirstNumberMed);
                record.addMedicineAirflow(R.id.editTextSecondNumberMed);
                record.addMedicineAirflow(R.id.editTextThirdNumberMed);
                record.setComment(Integer.toString(R.id.editTextComment));
                record.setDate(calendar.getTime());
                record.setType(2);
                break;
        }
        Singleton.getInstance().addRecord(record);
    }

}