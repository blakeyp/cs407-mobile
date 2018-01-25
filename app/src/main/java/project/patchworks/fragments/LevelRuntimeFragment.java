package project.patchworks.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import project.patchworks.R;
import project.patchworks.activities.ControllerActivity;
import project.patchworks.utils.Connection;
import project.patchworks.views.TouchpadView;

public class LevelRuntimeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Connection connection;
    private TouchpadView touchpadView;

    private Button actionButton;

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

        touchpadView = (TouchpadView) view.findViewById(R.id.touchPad);
        touchpadView.setDetector(new GestureDetector(touchpadView.getContext(), new scrollListener(touchpadView)));

        actionButton = (Button) view.findViewById(R.id.actionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                SpikeTrapFragment fragment2 = new SpikeTrapFragment();
                fragmentTransaction2.replace(R.id.fullscreen_content, fragment2);
                fragmentTransaction2.commit();

            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // must be implemented by activity using this fragment
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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