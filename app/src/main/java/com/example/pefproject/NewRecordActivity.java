package com.example.pefproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewRecordActivity extends AppCompatActivity {
    private final String logTag = "com.example.pefproject.APP_NewRecordActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
    }
}