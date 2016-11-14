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

public class humidity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList<String> data = new ArrayList();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        //set up sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(mSensor == null)
            data.add("Sensor not supported for this device.");

        //set up list
        listview = (ListView) findViewById(R.id.humidity_listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        data.clear();
        data.add(Float.toString(sensorEvent.values[0]) + "%");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
