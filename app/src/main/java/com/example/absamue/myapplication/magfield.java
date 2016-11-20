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
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class magfield extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private ArrayList<String> data = new ArrayList();
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
        setContentView(R.layout.activity_magfield);

        //set up sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mSensor == null)
            data.add("Sensor not supported for this device.");

        //set up list
        listview = (ListView) findViewById(R.id.magfield_listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);


        //set up graph
        graph = (GraphView) findViewById(R.id.magfield_graph);

        //set a legend since we have multiple lines
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setFixedPosition(0,0);

        //set up series parameters
        seriesX = new LineGraphSeries<>();
        seriesX.setTitle("X axis");
        seriesX.setColor(Color.RED);
        seriesX.setDrawDataPoints(true);

        seriesY = new LineGraphSeries<>();
        seriesY.setTitle("Y axis");
        seriesY.setColor(Color.BLUE);
        seriesY.setDrawDataPoints(true);

        seriesZ = new LineGraphSeries<>();
        seriesZ.setTitle("Z axis");
        seriesZ.setColor(Color.YELLOW);
        seriesZ.setDrawDataPoints(true);

        //add series to graph
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
                    return super.formatLabel(value, isValueX) + "μT";
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
        data.add("X axis: " + Float.toString(sensorEvent.values[0]) + " μT");
        data.add("Y axis: " + Float.toString(sensorEvent.values[1]) + " μT");
        data.add("Z axis: " + Float.toString(sensorEvent.values[2]) + " μT");
        adapter.notifyDataSetChanged();

        //get current values
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
