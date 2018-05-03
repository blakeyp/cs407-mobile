package patchworks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;
import patchworks.views.TouchpadView;

public class LevelRuntimeFragment extends Fragment {

    public static String captured = "uo";   // should be null

    private Connection connection;
    private TouchpadView touchpadView;

    private static Button captureButton;

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
        View view = inflater.inflate(R.layout.fragment_level_runtime, container, false);

        ControllerActivity.backButton.setVisibility(View.GONE);

        touchpadView = (TouchpadView) view.findViewById(R.id.touchPad);
        touchpadView.setDetector(new GestureDetector(touchpadView.getContext(), new scrollListener(touchpadView)));

        captureButton = (Button) view.findViewById(R.id.captureButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("capture");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Fragment fragment = new SlimeFragment();

                if (captured.equals("ufo"))
                    fragment = new UFOFragment();

                transaction.replace(R.id.fullscreen_content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;

    }

    class scrollListener extends GestureDetector.SimpleOnGestureListener {

        TouchpadView mView;

        public scrollListener(TouchpadView t) {
            mView = t;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("touch", "down on touchpad");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {

            Log.d("touch", "scroll on touchpad: " + dx/mView.getWidth() + "," + dy/mView.getHeight());

            if (!ControllerActivity.controllerDebug)
                connection.sendMessage(dx/mView.getWidth() + "," +dy/mView.getHeight() );

            mView.offsetX = Math.round((mView.offsetX - dx)%mView.mPattern.getWidth());
            mView.offsetY = Math.round((mView.offsetY - dy)%mView.mPattern.getHeight());
            mView.invalidate();

            return true;

        }

    }

}