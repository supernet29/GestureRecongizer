package com.example.asuper.gesturerecognizer;

import java.util.LinkedList;
import java.util.List;

public class GestureSampleManager {
    private LinkedList<GestureSample> sampleList;

    public GestureSampleManager() {
       sampleList = new LinkedList();
    }

    public void addSample(GestureSample sample) {
        synchronized (sampleList){
            sampleList.add(sample);
        }
    }

    public List<GestureSample> getSampleList(){
        synchronized (sampleList){
            return sampleList;
        }
    }
}
