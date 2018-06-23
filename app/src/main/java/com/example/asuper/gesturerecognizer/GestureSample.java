package com.example.asuper.gesturerecognizer;

import java.util.LinkedList;

public class GestureSample {
    private LinkedList<float[]> sampleData;
    public GestureSample(){
        sampleData = new LinkedList<>();
    }

    public void addSensorData(float[] data){
        sampleData.add(data);
    }

    public LinkedList<float[]> getSampleData() {
        return sampleData;
    }

    public int getSampleLength() {
        return sampleData.size();
    }
}
