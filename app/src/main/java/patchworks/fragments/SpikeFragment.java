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
import android.widget.ImageView;
import android.widget.ProgressBar;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;

public class SpikeFragment extends Fragment {

    private Connection connection;
    private ImageView spikeView;

    private long lastFireTime;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spike, container, false);

        spikeView = view.findViewById(R.id.spike);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        spikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long now = System.currentTimeMillis();
                if (lastFireTime + 2000 > now)   // ignore fires less than 1 second apart
                    return;
                lastFireTime = now;
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("spike");
                spikeView.setAlpha(0.2f);
                progressBar.setProgress(0);
                cooldownTimer = new CooldownTimer(2000, 5);
                cooldownTimer.start();
            }
        });

        ControllerActivity.backButton.setVisibility(View.VISIBLE);

        return view;

    }

    class CooldownTimer extends CountDownTimer {

        public CooldownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/20);
            progressBar.setProgress(100-progress);
        }

        @Override
        public void onFinish() {
            progressBar.setProgress(100);
            spikeView.setAlpha(1.0f);
        }

    }

}