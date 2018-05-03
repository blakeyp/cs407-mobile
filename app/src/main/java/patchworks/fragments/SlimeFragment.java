package patchworks.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;
import patchworks.utils.ShakeDetector;

public class SlimeFragment extends Fragment {

    private Connection connection;

    private ImageButton leftButton;
    private ImageButton rightButton;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // retrieve established connection from activity
        if (!ControllerActivity.controllerDebug) {
            connection = ((ControllerActivity) getActivity()).getConnection();
            connection.debug();
        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				Log.d("SHAKE", "shakey shake");
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("jump");
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slime, container, false);

        ControllerActivity.backButton.setVisibility(View.VISIBLE);

        leftButton = (ImageButton) view.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("left");
            }
        });

        rightButton = (ImageButton) view.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("right");
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}