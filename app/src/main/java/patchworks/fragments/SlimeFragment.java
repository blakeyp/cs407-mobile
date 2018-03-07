package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;

public class SlimeFragment extends Fragment {

    private Connection connection;

    private ImageButton leftButton;
    private ImageButton rightButton;

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

}