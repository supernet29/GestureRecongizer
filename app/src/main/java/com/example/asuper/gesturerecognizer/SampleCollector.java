package com.example.asuper.gesturerecognizer;

public abstract class SampleCollector {
    private GestureSample sample;
    private int length;
    private boolean record;

    public SampleCollector(int length) {
        this.sample = new GestureSample();
        this.length = length;
        this.record = false;
    }

    public void record(){
        record = true;
    }

    public void addSensorData(float[] data){
        if(record) {
            sample.addSensorData(data);
            if (sample.getSampleLength() == length) {
                record = false;
                onSampleCollected(sample);
                sample = new GestureSample();
            }
        }
    }

    public abstract void onSampleCollected(GestureSample sample);
}
