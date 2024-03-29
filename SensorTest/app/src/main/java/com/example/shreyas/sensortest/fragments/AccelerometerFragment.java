package com.example.shreyas.sensortest.fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shreyas.sensortest.R;

import java.util.ArrayList;
import java.util.Random;


public class AccelerometerFragment extends Fragment implements SensorEventListener {

    private TextView accelerationValueDisplay;
    private ImageView randomNumberImageDisplay;
    private TextView shakeView;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private ArrayList<Integer> randomStrings = new ArrayList<>();

    private float[] gravity = new float[3];

    private Random rand;

    public AccelerometerFragment() {
        // Required empty public constructor
        rand = new Random();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accelerometer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accelerationValueDisplay = view.findViewById(R.id.accelerometer_values);
        randomNumberImageDisplay = view.findViewById(R.id.random_string);
        shakeView = view.findViewById(R.id.shake_view);

        populateRandomStrings();

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        try {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } catch (NullPointerException e) {
            accelerationValueDisplay.setText("No Accelerometer Sensor Found");
        }
    }

    public void populateRandomStrings() {
        randomStrings.add(R.drawable.rock);
        randomStrings.add(R.drawable.paper);
        randomStrings.add(R.drawable.scissor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerationValueDisplay.setText(String.format("%.2f %.2f %.2f", event.values[0], event.values[1], event.values[2]));

            if (Math.abs(event.values[0] - gravity[0]) > 20 || Math.abs(event.values[1] - gravity[1]) > 20 || Math.abs(event.values[2] - gravity[2]) > 20) {
                randomNumberImageDisplay.setImageResource(randomStrings.get(rand.nextInt(3)));
                shakeView.setText("Shake Detected!");
            } else {
                shakeView.setText("");
            }
            gravity = event.values.clone();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
