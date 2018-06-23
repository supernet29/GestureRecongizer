package com.example.asuper.gesturerecognizer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity
{

    private SensorManager sensorManager;
    private Timer timer;
    private SensorTask sensorTask;
    private SampleCollector sampleCollector;
    private SampleManager sampleManager;

    private TextView countTextView;
    private Button collectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = findViewById(R.id.sample_size_txt);
        collectButton = findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleCollector.record();
            }
        });

        timer = new Timer();
        sampleManager = new SampleManager();
        sensorTask = new SensorTask(){
            @Override
            protected void onSensorData(float[] acceleratorData) {
                sampleCollector.addSensorData(acceleratorData);
                StringBuilder builder = new StringBuilder();
                builder.append("X: ") .append(acceleratorData[0])
                       .append(", Y: ") .append(acceleratorData[1])
                       .append(", Z: ") .append(acceleratorData[2]);
                Log.i("SensorOnTimer", builder.toString());
            }
        };

        sampleCollector = new SampleCollector(5) {
            @Override
            public void onSampleCollected(GestureSample sample) {
                sampleManager.addSample(sample);
                showSampleCount(sampleManager.getSampleList().size());
            }
        };
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null) {
            Log.i("Sensor", "There are no sensor");
        }else{
            sensorManager.registerListener(sensorTask, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            timer.schedule(sensorTask, 0, 300);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(sensorTask);
        timer.cancel();
    }

    public void showSampleCount(int size) {
        final int count = size;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countTextView.setText("Sample number: " + count);
            }
        });
    }
}
