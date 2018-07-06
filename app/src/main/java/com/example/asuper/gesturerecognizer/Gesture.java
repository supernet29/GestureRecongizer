package com.example.asuper.gesturerecognizer;

public class Gesture {
    public final static Gesture NO_GESUTRE = new Gesture("NO_GESTURE", -1);

    private String name;
    private int code;

    public Gesture(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
