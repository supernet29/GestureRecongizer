package com.example.asuper.gesturerecognizer.recognizer;

import com.example.asuper.gesturerecognizer.GestureSample;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import java.util.List;

public class RecognizeRunnable implements  Runnable{
    private MultiLayerNetwork recognizer;
    private List<GestureSample> gestureSampleList;

    public RecognizeRunnable(MultiLayerNetwork recognizer, List<GestureSample> gestureSampleList){
        this.recognizer =recognizer;
        this.gestureSampleList = gestureSampleList;
    }

    @Override
    public void run() {

        for(int i = 0; i < 100; i++) {
            recognizer.fit();
        }
    }
}
