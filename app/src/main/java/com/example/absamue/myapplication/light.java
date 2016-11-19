package com.example.absamue.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class light extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList<String> data = new ArrayList();
    private ArrayAdapter adapter;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;
    private int current = 0;
    private float current_data;
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

          //set up sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(mSensor == null)
            data.add("Sensor not supported for this device.");

        //set up list
        listview = (ListView) findViewById(R.id.light_listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);

        //set up graph
        graph = (GraphView) findViewById(R.id.light_graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);

        //set axis labels
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX) + "s";
                } else {
                    // show lx for y values
                    return super.formatLabel(value, isValueX) + " lx";
                }
            }
        });

        //set bounds of graph
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
    }

    //update text data when it changes and get current data
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //update list for text field
        data.clear();
        data.add(Float.toString(sensorEvent.values[0]) + " lx");

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

        //update graph one a second
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                data.clear();
                data.add(Float.toString(current_data));

                series.appendData(new DataPoint(current, current_data), true, 10);
                current += 1;

            }
        }, 1000, 1000);

   }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
