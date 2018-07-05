package com.example.asuper.gesturerecognizer;

import java.util.LinkedList;

public class GestureSample {
    private LinkedList<float[]> sampleData;
    private int gestureCode;

    public GestureSample(){
        this.sampleData = new LinkedList<>();
        this.gestureCode = -1;
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

    public int getGestureCode() {
        return gestureCode;
    }

    public void setGestureCode(int gestureCode) {
        this.gestureCode = gestureCode;
    }
}
