package com.example.absamue.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class temperature extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList <String> data = new ArrayList();
    private ArrayAdapter adapter;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;
    private int current = 0;
    private float current_data;
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        if(mSensor == null)
            data.add("Sensor not supported for this device.");
        listview = (ListView) findViewById(R.id.templist);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);

        //set up graph
        graph = (GraphView) findViewById(R.id.temp_graph);
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        graph.addSeries(series);

        if (!(mSensor == null)) {
            //set axis labels
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        // show normal x values
                        return super.formatLabel(value, isValueX) + "s";
                    } else {
                        // show lx for y values
                        return super.formatLabel(value, isValueX) + " °C";
                    }
                }
            });

            //set bounds of graph
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(10);
        }
        else{
            View g = findViewById(R.id.temp_graph);
            g.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        data.clear();
        data.add(Float.toString(sensorEvent.values[0]) + "°C");

        //get data for use in graph
        current_data = sensorEvent.values[0];

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (!(mSensor == null)) {
            //update graph one a second
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    series.appendData(new DataPoint(current, current_data), true, 10);
                    current += 1;

                }
            }, 1000, 1000);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
