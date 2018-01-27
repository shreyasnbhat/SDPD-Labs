package com.example.shreyas.sensortest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.shreyas.sensortest.fragments.AccelerometerFragment;
import com.example.shreyas.sensortest.fragments.CompassFragment;
import com.example.shreyas.sensortest.fragments.ProximityFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentFrame;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction ft0 = fragmentManager.beginTransaction();
                    ft0.replace(R.id.frame, new CompassFragment());
                    ft0.commit();
                    return true;
                case R.id.navigation_dashboard:
                    FragmentTransaction ft1 = fragmentManager.beginTransaction();
                    ft1.replace(R.id.frame, new ProximityFragment());
                    ft1.commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentTransaction ft2 = fragmentManager.beginTransaction();
                    ft2.replace(R.id.frame, new AccelerometerFragment());
                    ft2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        setContentView(R.layout.activity_main);
        fragmentFrame = findViewById(R.id.frame);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, new CompassFragment());
        ft.commit();

    }
}
