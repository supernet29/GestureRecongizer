package com.example.asuper.gesturerecognizer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.asuper.gesturerecognizer.ui.gesturesample.ListAdapter;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity
{

    private SensorManager sensorManager;
    private Timer timer;
    private SensorTask sensorTask;
    private SampleCollector sampleCollector;
    private GestureSampleManager gestureSampleManager;

    private TextView countTextView;
    private Button collectButton;

    private XYPlot plot;
    private Number[] domainLabels;
    private LineAndPointFormatter axFormater;
    private LineAndPointFormatter ayFormater;
    private LineAndPointFormatter azFormater;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter adapter;

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

        sampleCollector = new SampleCollector(10) {
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
        domainLabels = new Number[]{0, 1, 2, 3, 4};
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
        LinkedList<Gesture> gestures = new LinkedList<>();
        gestures.add(new Gesture("Right", 0));
        gestures.add(new Gesture("Left", 1));
        adapter.setGestures(gestures);
        recyclerView.setAdapter(adapter);

        // NeuralNetwork
        /*
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(110)
                .l2(0.005)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(0.05))
                .list()
                .layer(0, new ConvolutionLayer.Builder(3, 3)
                        .nIn(1)
                        .stride(1, 1)
                        .nOut(20)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(1, new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2, 2)
                        .build())
                .layer(2, new ConvolutionLayer.Builder(3, 3)
                        //Note that nIn need not be specified in later layers
                        .stride(1, 1)
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(3, new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2,2)
                        .build())
                .layer(4, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(100).build())
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(2)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(10, 3, 1))
                .backprop(true).pretrain(false)
                .build();

        recognizer = new MultiLayerNetwork(conf);
        recognizer.init();
        */
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
