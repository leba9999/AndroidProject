package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Record record = new Record();
    }

    public void onNewRecordClicked (View view) {
        Intent intent = new Intent(this, NewRecordActivity.class);
        startActivity(intent);
    }

    public void onOldRecordClicked (View view) {
        Intent intent = new Intent(this, OldRecordActivity.class);
        startActivity(intent);
    }
}