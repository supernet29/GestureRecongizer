package com.example.asuper.gesturerecognizer;

import java.util.LinkedList;
import java.util.List;

public class SampleManager {
    private LinkedList<GestureSample> sampleList;

    public SampleManager() {
       sampleList = new LinkedList();
    }

    public synchronized void addSample(GestureSample sample) {
        sampleList.add(sample);
    }

    public synchronized List<GestureSample> getSampleList(){
        return sampleList;
    }
}
