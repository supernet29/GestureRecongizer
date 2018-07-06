package com.example.asuper.gesturerecognizer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.asuper.gesturerecognizer.ui.gesturesample.ListAdapter;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity
{

    private SensorManager sensorManager;
    private Timer timer;
    private SensorTask sensorTask;
    private SampleCollector sampleCollector;
    private GestureSampleManager gestureSampleManager;
    private GestureManager gestureManager;

    private TextView countTextView;
    private Button collectButton;
    private Button trainingButton;

    private XYPlot plot;
    private LineAndPointFormatter axFormater;
    private LineAndPointFormatter ayFormater;
    private LineAndPointFormatter azFormater;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter adapter;

    private final static int sensorSize = 3;
    private final static int length = 10;

    private MultiLayerNetwork recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = findViewById(R.id.sample_size_txt);
        collectButton = findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleCollector.record();
            }
        });

        gestureSampleManager = new GestureSampleManager();
        gestureManager = new GestureManager();
        gestureManager.addGesture("Right");
        gestureManager.addGesture("Left");

        sampleCollector = new SampleCollector(length) {
            @Override
            public void onSampleCollected(GestureSample sample) {
                gestureSampleManager.addSample(sample);
                updateUIOnSample(gestureSampleManager.getSampleList().size());
            }
        };

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Plot
        plot = findViewById(R.id.sample_plot);
        axFormater = new LineAndPointFormatter(this, R.xml.ax_formatter);
        ayFormater = new LineAndPointFormatter(this, R.xml.ay_formatter);
        azFormater = new LineAndPointFormatter(this, R.xml.az_formatter);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM);

        // sample manager;
        recyclerView = findViewById(R.id.sample_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        View.OnClickListener elementListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureSample sample = (GestureSample) v.getTag();
                LinkedList<float[]> data = sample.getSampleData();
                int size = data.size();
                Number[] ax = new Number[size];
                Number[] ay = new Number[size];
                Number[] az = new Number[size];
                int i = 0;
                for(float[] point : data) {
                    ax[i] = point[0];
                    ay[i] = point[1];
                    az[i] = point[2];
                    i++;
                }
                XYSeries axSeries = new SimpleXYSeries(Arrays.asList(ax), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "AX");
                XYSeries aySeries = new SimpleXYSeries(Arrays.asList(ay), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "AY");
                XYSeries azSeries = new SimpleXYSeries(Arrays.asList(az), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "AZ");
                plot.clear();
                plot.addSeries(axSeries, axFormater);
                plot.addSeries(aySeries, ayFormater);
                plot.addSeries(azSeries, azFormater);
                plot.redraw();
            }
        };
        adapter = new ListAdapter(elementListener);
        adapter.setGestures(gestureManager.getGestureList());
        recyclerView.setAdapter(adapter);

        // NeuralNetwork
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Updater.ADAM)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(sensorSize*length)
                        .nOut(50)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(50)
                        .nOut(50)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new OutputLayer.Builder()
                        .nIn(50)
                        .nOut(gestureManager.getGestureLength())
                        .activation(Activation.SOFTMAX)
                        .build())
                .backprop(true)
                .build();
        recognizer = new MultiLayerNetwork(conf);
        recognizer.init();

        trainingButton = findViewById(R.id.training_btn);
        trainingButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {
               trainingButton.setEnabled(false);
               List<GestureSample> gestureSamples =  gestureSampleManager.getSampleList();
               final NDArray input = new NDArray(gestureSamples.size(), sensorSize * length);
               NDArray output = new NDArray(gestureSamples.size(), gestureManager.getGestureLength());
               for(int i = 0; i < gestureSamples.size(); i++){
                   GestureSample sample = gestureSamples.get(i);
                   for(int j = 0; j < sample.getSampleLength(); j++) {
                       float[] data = sample.getSampleData().get(j);
                       input.putScalar(i, j * 3 + 0, data[0]);
                       input.putScalar(i, j * 3 + 1, data[1]);
                       input.putScalar(i, j * 3 + 2, data[2]);
                   }

                   for(int k = 0; k < gestureManager.getGestureLength(); k++) {
                       if(k == sample.getGestureCode()){
                           output.putScalar(i, k, 1);
                       }else{
                           output.putScalar(i, k, 0);
                       }
                   }
               }

               final DataSet trainingDataSet = new DataSet(input, output);

               AsyncTask trainingTask = new AsyncTask() {
                   @Override
                   protected Object doInBackground(Object[] objects) {
                       for(int i =0; i < 500; i++) {
                          recognizer.fit(trainingDataSet);
                       }
                       return recognizer;
                   }

                   @Override
                   protected void onPostExecute(Object o) {
                       super.onPostExecute(o);
                       int[] out = recognizer.predict(input);
                       Toast.makeText(getApplicationContext(), "TrainingComplete", Toast.LENGTH_LONG).show();
                       for(int i = 0; i < out.length; i++) {
                           Log.i("Predict", i + " : " + out[i]);
                       }
                       trainingButton.setEnabled(true);
                   }
               };
               trainingTask.execute();
           }
       });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null) {
            Log.i("Sensor", "There are no sensor");
        }else{
            timer =  new Timer();
            sensorTask = new SensorTask(){
                @Override
                protected void onSensorData(float[] acceleratorData) {
                    sampleCollector.addSensorData(acceleratorData);
                    StringBuilder builder = new StringBuilder();
                    builder.append("X: ") .append(acceleratorData[0])
                           .append(", Y: ") .append(acceleratorData[1])
                           .append(", Z: ") .append(acceleratorData[2]);
                    Log.i("SensorOnTimer", builder.toString());
                }
            };
            sensorManager.registerListener(sensorTask, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            timer.schedule(sensorTask, 0, 100);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        timer.cancel();
        sensorManager.unregisterListener(sensorTask);
    }

    public void updateUIOnSample(int size) {
        final int count = size;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countTextView.setText("Sample number: " + count);
                adapter.setSamples(gestureSampleManager.getSampleList());
            }
        });
    }
}
