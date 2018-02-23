package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;

public class SpikeTrapFragment extends Fragment {

    private Connection connection;

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
        View view = inflater.inflate(R.layout.fragment_spike_trap, container, false);

        ControllerActivity.backButton.setVisibility(View.VISIBLE);

        return view;

    }

}