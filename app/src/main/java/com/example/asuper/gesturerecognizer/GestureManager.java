package com.example.asuper.gesturerecognizer;

import java.util.LinkedList;
import java.util.List;

public class GestureManager {
    private List<Gesture> gestureList;
    private int nextCode;

    public GestureManager(){
        this.gestureList = new LinkedList<>();
        this.nextCode = 0;
    }

    public void addGesture(String name){
        gestureList.add(new Gesture(name, nextCode));
        nextCode++;
    };

    public Gesture findGestureByCode(int code) {
        for(Gesture gesture : gestureList) {
            if(gesture.getCode() == code)
                return gesture;
        }
        return null;
    }

    public List<Gesture> getGestureList(){
        return gestureList;
    }
}
