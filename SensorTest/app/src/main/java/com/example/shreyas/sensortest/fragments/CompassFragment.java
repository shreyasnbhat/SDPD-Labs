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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shreyas.sensortest.R;

public class CompassFragment extends Fragment implements SensorEventListener {

    private TextView directionTextView, degreeTextView;
    private ImageView compassView;

    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private Sensor accelerometerSensor;

    // Accelerometer Values
    private float[] gravity = new float[3];
    private boolean accelerometerSensorSet = false;

    // Magnetometer Values
    private float[] magneticStrength = new float[3];
    private boolean magneticSensorSet = false;

    private float[] rotationMatrix = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        degreeTextView = view.findViewById(R.id.degree_text);
        directionTextView = view.findViewById(R.id.direction_text);
        compassView = view.findViewById(R.id.compass);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        try {
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } catch (NullPointerException e) {
            degreeTextView.setText("No Gyroscope or Accelerometer Sensor Found");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticStrength = event.values.clone();
                magneticSensorSet = true;
            } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values.clone();
                accelerometerSensorSet = true;
            }

            if (magneticSensorSet && accelerometerSensorSet) {
                SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magneticStrength);
                SensorManager.getOrientation(rotationMatrix, mOrientation);
                float azimuthInRadians = mOrientation[0];
                float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
                degreeTextView.setText(String.format("%.2f", azimuthInDegress));
                directionTextView.setText(getDirectionFromAngle(azimuthInDegress));
                RotateAnimation ra = new RotateAnimation(
                        mCurrentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                ra.setDuration(200);
                ra.setFillAfter(true);
                compassView.startAnimation(ra);
                mCurrentDegree = -azimuthInDegress;
            }
        }
    }

    public String getDirectionFromAngle(double angle) {
        if (angle < 22.5 || angle > 337.5) {
            return "North";
        } else if (angle <= 67.5 && angle >= 22.5) {
            return "North-East";
        } else if (angle < 112.5 && angle > 67.5) {
            return "East";
        } else if (angle <= 157.5 && angle >= 112.5) {
            return "South-East";
        } else if (angle < 202.5 && angle > 157.5) {
            return "South";
        } else if (angle <= 247.5 && angle >= 202.5) {
            return "South-West";
        } else if (angle < 292.5 && angle > 247.5) {
            return "West";
        } else if (angle <= 337.5 && angle >= 292.5) {
            return "North-West";
        }
        return "Error";
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, magneticSensor);
        sensorManager.unregisterListener(this, accelerometerSensor);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
