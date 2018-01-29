package com.example.shreyas.sensortest.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shreyas.sensortest.R;


public class ProximityFragment extends Fragment implements SensorEventListener {

    private TextView distanceView, distanceInWordsView;
    private LinearLayout proximityFrame;
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private Integer colorFrom;
    private Integer colorTo;

    private float distancePrevious;

    private ValueAnimator colorAnimation;
    private ValueAnimator colorAnimationReverse;
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animator) {
            proximityFrame.setBackgroundColor((int) animator.getAnimatedValue());
        }

    };

    public ProximityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proximity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        colorFrom = getResources().getColor(R.color.green);
        colorTo = getResources().getColor(R.color.red);

        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationReverse = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);

        distanceView = view.findViewById(R.id.distance);
        distanceInWordsView = view.findViewById(R.id.distance_words);
        proximityFrame = view.findViewById(R.id.layout_frame);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        try {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        } catch (NullPointerException e) {
            distanceView.setText("No Proximity Sensor Found");
            distanceInWordsView.setText("");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            distanceView.setText(String.format("%.2f", event.values[0]) + " cm");
            changeBackgroundByDistance(event.values[0]);
            distancePrevious = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, proximitySensor);
        colorAnimation.removeUpdateListener(animatorUpdateListener);
        colorAnimation.removeUpdateListener(animatorUpdateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        colorAnimation.addUpdateListener(animatorUpdateListener);
        colorAnimationReverse.addUpdateListener(animatorUpdateListener);
    }

    public void changeBackgroundByDistance(float distance) {
        if (distance < 0.5) {
            distanceInWordsView.setText("Near");
        } else {
            distanceInWordsView.setText("Far");
        }
        if (distancePrevious - distance > 2) {
            colorAnimation.setDuration(500); // milliseconds
            colorAnimation.start();
        } else if (distancePrevious - distance < 2) {
            colorAnimationReverse.setDuration(500); // milliseconds
            colorAnimationReverse.start();
        }
    }

}
