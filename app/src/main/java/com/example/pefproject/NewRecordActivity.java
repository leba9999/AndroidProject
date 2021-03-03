package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        Singleton.getInstance().loadData(this);

        ArrayList<Record> records = Singleton.getInstance().getRecording();
        dateTextView = findViewById(R.id.textViewDate);
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


        Bundle b = getIntent().getExtras();
        int i = b.getInt(OldRecordActivity.EXTRA, -1);
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

        }

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

    }

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

    public void onSaveButtonClicked(View view) {
        saveRecords();
        Singleton.getInstance().saveData(this);

        Bundle b = getIntent().getExtras();
        int i = b.getInt(OldRecordActivity.EXTRA, -1);

        if (i == -1) {
            nextActivity = new Intent(this, MainActivity.class);
            //nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        } else {
            nextActivity = new Intent(this, OldRecordActivity.class);
            nextActivity.putExtra(OldRecordActivity.EXTRA, -1);
        }
        startActivity(nextActivity);

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
        //EditText firstNumberNormal = (EditText) firstNumberNormal;
        //EditText secondNumberNormal = (EditText) findViewById(R.id.editTextSecondNumberNormal);
        //EditText thirdNumberNormal = (EditText) findViewById(R.id.editTextThirdNumberNormal);
        //EditText firstNumberMed = (EditText) findViewById(R.id.editTextFirstNumberMed);
        //EditText secondNumberMed = (EditText) findViewById(R.id.editTextSecondNumberMed);
        //EditText thirdNumberMed = (EditText) findViewById(R.id.editTextThirdNumberMed);
        //EditText comment = (EditText) findViewById(R.id.editTextComment);

        record.addNormalAirflow(Integer.parseInt(firstNumberNormal.getText().toString()));
        record.addNormalAirflow(Integer.parseInt(secondNumberNormal.getText().toString()));
        record.addNormalAirflow(Integer.parseInt(thirdNumberNormal.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(firstNumberMed.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(secondNumberMed.getText().toString()));
        record.addMedicineAirflow(Integer.parseInt(thirdNumberMed.getText().toString()));
        record.setComment(commentText.getText().toString());
        record.setDate(date);
        Singleton.getInstance().addRecord(record);
    }

}