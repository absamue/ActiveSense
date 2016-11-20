package com.example.absamue.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class accelerometer extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList <String> data = new ArrayList();
    private ArrayAdapter adapter;
    private GraphView graph;
    private LineGraphSeries<DataPoint> seriesX;
    private LineGraphSeries<DataPoint> seriesY;
    private LineGraphSeries<DataPoint> seriesZ;
    private int current = 0;
    private float current_data_X;
    private float current_data_Y;
    private float current_data_Z;
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        //set up sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mSensor == null)
            data.add("Sensor not supported for this device.");

        //set up list
        listview = (ListView) findViewById(R.id.accel_listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);

        //set up graph
        graph = (GraphView) findViewById(R.id.accel_graph);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setFixedPosition(0,0);

        seriesX = new LineGraphSeries<>();
        seriesY = new LineGraphSeries<>();
        seriesZ = new LineGraphSeries<>();

        //set up color and points
        seriesX.setColor(Color.RED);
        seriesX.setDrawDataPoints(true);
        seriesX.setTitle("X axis");

        seriesY.setColor(Color.BLUE);
        seriesY.setDrawDataPoints(true);
        seriesY.setTitle("Y axis");

        seriesZ.setColor(Color.YELLOW);
        seriesZ.setDrawDataPoints(true);
        seriesZ.setTitle("Z axis");

        graph.addSeries(seriesX);
        graph.addSeries(seriesY);
        graph.addSeries(seriesZ);


        //set axis labels
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show seconds for x values
                    return super.formatLabel(value, isValueX) + "s";
                } else {
                    // show normal for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        //set bounds of graph
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        data.clear();
        data.add("X Axis: " + Float.toString(sensorEvent.values[0]));
        data.add("Y Axis: " + Float.toString(sensorEvent.values[1]));
        data.add("Z Axis: " + Float.toString(sensorEvent.values[2]));
        adapter.notifyDataSetChanged();

        current_data_X = sensorEvent.values[0];
        current_data_Y = sensorEvent.values[1];
        current_data_Z = sensorEvent.values[2];

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
                seriesX.appendData(new DataPoint(current, current_data_X), true, 10);
                seriesY.appendData(new DataPoint(current, current_data_Y), true, 10);
                seriesZ.appendData(new DataPoint(current, current_data_Z), true, 10);
                current += 1;

            }
        }, 1000, 1000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);

        mTimer.purge();
    }
}
