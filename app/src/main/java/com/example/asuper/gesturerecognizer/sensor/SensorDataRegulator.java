package com.example.asuper.gesturerecognizer.sensor;

import java.util.LinkedList;

public class SensorDataRegulator {
    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;
    private LinkedList<float[]> sensorDataList;

    public SensorDataRegulator() {
        sensorDataList = new LinkedList<>();
    }

    public void pushData(float[] values) {

        synchronized (sensorDataList){
            sensorDataList.add(values);
        }
    }

    public float[] getAverageData() {
        float x = 0, y = 0, z = 0;
        int size;
        synchronized (sensorDataList){
            size = sensorDataList.size();
            for(float[] singleSense : sensorDataList) {
                x += singleSense[X];
                y += singleSense[Y];
                z += singleSense[Z];
            }
            sensorDataList.clear();
        }
        return new float[] { x / size, y / size, z / size };
    }
}
