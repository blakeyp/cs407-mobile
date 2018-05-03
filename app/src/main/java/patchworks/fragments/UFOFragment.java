package patchworks.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import it.sephiroth.android.library.tooltip.Tooltip;
import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;

public class UFOFragment extends Fragment {

    private Connection connection;

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private SensorEventListener rvListener;

    private long lastFireTime;

    private ImageView ufoView;

    ProgressBar progressBar;

    CooldownTimer cooldownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // retrieve established connection from activity
        if (!ControllerActivity.controllerDebug) {
            connection = ((ControllerActivity) getActivity()).getConnection();
            connection.debug();
        }

        // set up gyroscope listener
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ufo, container, false);

        ufoView = view.findViewById(R.id.ufo);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        ufoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long now = System.currentTimeMillis();
                if (lastFireTime + 1000 > now)   // ignore fires less than 1 second apart
                    return;
                lastFireTime = now;
                Log.d("UFO", "fire!");
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("fire");

                progressBar.setProgress(0);
                cooldownTimer = new CooldownTimer(1000, 5);
                cooldownTimer.start();

            }
        });

        ControllerActivity.backButton.setVisibility(View.VISIBLE);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // Create a listener
        rvListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_Y,   // landscape orientation
                        SensorManager.AXIS_MINUS_X,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                Log.d("GYRO", "rotation: "+orientations[2]);

                ufoView.setRotation(orientations[2]);

                float ang = orientations[2];

                if (ang > 15 && ang <= 30) {
                    Log.d("GYRO", "right");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("right");
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);   // right
                    //ufoView.setRotation(30f);
                } else if (ang > 30) {
                    Log.d("GYRO", "right2");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("right2");
                } else if (ang < -15 && ang >= -30) {
                    Log.d("GYRO", "left");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("left");
                    //getWindow().getDecorView().setBackgroundColor(Color.BLUE);   // left
                    //ufoView.setRotation(-30f);
                } else if (ang < -30) {
                    Log.d("GYRO", "left2");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("left2");
                } else {
                    Log.d("GYRO", "stop");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("stop");
                    //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    //ufoView.setRotation(0f);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}

        };

        // Register it
        sensorManager.registerListener(rvListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(rvListener);
    }

    class CooldownTimer extends CountDownTimer {

        public CooldownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/10);
            progressBar.setProgress(100-progress);
        }

        @Override
        public void onFinish() {
            progressBar.setProgress(100);
        }

    }

}