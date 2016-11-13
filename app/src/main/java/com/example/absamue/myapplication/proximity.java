package com.example.absamue.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class proximity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList<String> data = new ArrayList();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        listview = (ListView) findViewById(R.id.prox_listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        data.clear();
        data.add(Float.toString(sensorEvent.values[0]) + " cm");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}