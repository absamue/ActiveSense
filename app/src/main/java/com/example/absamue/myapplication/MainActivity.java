package com.example.absamue.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get listview
        ListView listview = (ListView) findViewById(R.id.listview);

        //find all the sensors
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        //add each name of sensor to list
        ArrayList<String> list = new ArrayList();
        for(Sensor sens : sensorList){
            list.add(sens.getName());
        }

        //set listview adapter to created list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        //click on adapter opens new activity
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                switch(position){
                    case 0: Intent newActivity0 = new Intent(MainActivity.this, accelerometer.class);
                        startActivity(newActivity0);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3: Intent newActivity3 = new Intent(MainActivity.this, temperature.class);
                        startActivity(newActivity3);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
            }
            @SuppressWarnings("unused")
            public void onClick(View v){
            }
        });
    }
}
