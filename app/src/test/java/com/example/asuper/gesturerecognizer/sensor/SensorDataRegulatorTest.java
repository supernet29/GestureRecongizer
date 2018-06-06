package com.example.asuper.gesturerecognizer.sensor;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SensorDataRegulatorTest {
    @Test
    public void correctAverageData() {
        SensorDataRegulator regulator = new SensorDataRegulator();
        regulator.pushData(new float[]{1, 1, 1});
        regulator.pushData(new float[]{1, 1, 1});
        regulator.pushData(new float[]{1, 1, 1});

        float[] data = regulator.getAverageData();
        assertThat(data, equalTo(new float[]{1, 1, 1}));
    }

    @Test
    public void listCleaningNeeded() {
        SensorDataRegulator regulator = new SensorDataRegulator();
        regulator.pushData(new float[]{1, 1, 1});
        regulator.pushData(new float[]{1, 1, 1});
        regulator.pushData(new float[]{1, 1, 1});
        regulator.getAverageData();

        regulator.pushData(new float[]{2, 1, 1});
        regulator.pushData(new float[]{2, 1, 1});
        regulator.pushData(new float[]{2, 1, 1});

        float[] data = regulator.getAverageData();
        assertThat(data, equalTo(new float[]{2, 1, 1}));
    }
}
