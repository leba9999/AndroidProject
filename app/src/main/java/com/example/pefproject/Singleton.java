package com.example.pefproject;

import java.util.ArrayList;

public class Singleton {
    private final String logTag = "com.example.pefproject.APP_Singleton.java";

    private static final Singleton instance = new Singleton();
    private ArrayList<Record> recording;

    private Singleton () {
        recording = new ArrayList<>();
    }

    public static Singleton getInstance() {
        return instance;
    }

    public void addRecord(Record record) {
        recording.add(record);
    }

    public ArrayList<Record> getRecording() {
        return recording;
    }

    public Record getRecord(int i) {
        return recording.get(i);
    }
}
