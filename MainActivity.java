package com.example.gyroscope;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView Y;
    private TextView X;
    private TextView Z;
    private SensorManager sM;
    private Sensor sensor;
    private SensorEventListener sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Y = findViewById(R.id.textY);
        X = findViewById(R.id.textX);
        Z = findViewById(R.id.textZ);

        sM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sM != null) sensor = sM.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sv = new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                float[] remmappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remmappedRotationMatrix);
                float[] orientations = new float[3];
                SensorManager.getOrientation(remmappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }
                Y.setText("" + (int) orientations[2]);
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_Y, remmappedRotationMatrix);
                float[] orientations3 = new float[3];
                SensorManager.getOrientation(remmappedRotationMatrix, orientations3);
                for (int i = 0; i < 3; i++) {
                    orientations3[i] = (float) (Math.toDegrees(orientations3[i]));
                }
                Z.setText("" + (int) orientations3[0]);
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Z, SensorManager.AXIS_X, remmappedRotationMatrix);
                float[] orientations2 = new float[3];
                SensorManager.getOrientation(remmappedRotationMatrix, orientations2);
                for (int i = 0; i < 3; i++) {
                    orientations2[i] = (float) (Math.toDegrees(orientations2[i]));
                }
                X.setText("" + (int) orientations2[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sM.registerListener(sv, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sM.unregisterListener(sv);
    }
}