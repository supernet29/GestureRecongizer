package com.example.asuper.gesturerecognizer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.example.asuper.gesturerecognizer.sensor.SensorDataRegulator;

import java.util.TimerTask;

public abstract class SensorTask extends TimerTask implements SensorEventListener{
    private SensorDataRegulator regulator;

    public SensorTask() {
        regulator = new SensorDataRegulator();
    }

    @Override
    public void run() {
        float[] acceleratorData = regulator.getAverageData();
        onSensorData(acceleratorData);
    }

    protected abstract void onSensorData(float[] acceleratorData);

    @Override
    public void onSensorChanged(SensorEvent event) {
        regulator.pushData(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
