package com.example.asuper.gesturerecognizer.recognizer;

import java.util.LinkedList;

public abstract class Window {
    private final int length;
    private LinkedList<float[]> dataList;
    private OnWindowReadyListener onWindowReadyListener;

    public Window(int length){
       this.length = length;
       this.dataList = new LinkedList<>();
       this.onWindowReadyListener = null;
    }

    public void update(float[] sensorData) {
        dataList.offer(sensorData);
        if(dataList.size() > length){
            dataList.poll();
        }
        if(dataList.size() == length && onWindowReadyListener != null){
            onWindowReadyListener.onWindowReady(dataList);
        }
    }

    public interface OnWindowReadyListener{
        void onWindowReady(LinkedList<float[]> dataList);
    }
}
