package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import patchworks.R;
import patchworks.activities.ControllerActivity;
import patchworks.utils.Connection;
import patchworks.utils.ScrollListener;
import patchworks.views.TouchpadView;

public class LevelEditorFragment extends Fragment {

    private Connection connection;
    private TouchpadView touchpadView;
    private ToggleButton eraserButton;
    private ToggleButton pencilButton;
    private ToggleButton grabButton;
    private ImageView actionButtonIndicator;
    private Button actionButton;
    private ScrollView tileDrawer;
    private HashMap<String, Integer> paletteIcons;
    private ArrayList<Button> tilePalette;
    private int mSelectedTile = 1;

    /****/
    // Can use this stuff if want to pass in parameters on creating fragment

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LevelEditorFragment() {}   // Required empty public constructor

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelEditorFragment newInstance(String param1, String param2) {
        LevelEditorFragment fragment = new LevelEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /****/

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

        // retrieve established connection from activity
        if (!ControllerActivity.controllerDebug) {
            connection = ((ControllerActivity) getActivity()).getConnection();
            connection.debug();
            Log.d("MYDEBUG", "has connection");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_editor, container, false);

        ControllerActivity.backButton.setVisibility(View.GONE);   // hide back button
        //ControllerActivity.helpButton.setVisibility(View.GONE);

        paletteIcons = new HashMap();
        paletteIcons.put("basic 0", R.drawable.tx_tile_solid);
        paletteIcons.put("basic 1", R.drawable.tx_tile_semisolid);
        paletteIcons.put("basic 2", R.drawable.tx_tile_ladder);
        paletteIcons.put("basic 3", R.drawable.tx_tile_crate);
        paletteIcons.put("bg 0", R.drawable.tx_tile_bush_01);
        paletteIcons.put("bg 1", R.drawable.tx_tile_cloud_01);
        paletteIcons.put("bg 2", R.drawable.tx_tile_flower);
        paletteIcons.put("bg 3", R.drawable.tx_tile_mountain);
        paletteIcons.put("tech 0", R.drawable.tx_tile_startpoint);
        paletteIcons.put("tech 1", R.drawable.tx_tile_doughnut);
        paletteIcons.put("tech 2", R.drawable.tx_tile_ufo);
        paletteIcons.put("tech 3", R.drawable.tx_tile_spikes);

        touchpadView = (TouchpadView) view.findViewById(R.id.touchPad);
        touchpadView.setDetector(new GestureDetector(touchpadView.getContext(), new ScrollListener(touchpadView, connection)));

        eraserButton = (ToggleButton) view.findViewById(R.id.eraserButton);
        pencilButton = (ToggleButton) view.findViewById(R.id.pencilButton);
        pencilButton.setChecked(true);   // on by default
        actionButtonIndicator = (ImageView) view.findViewById(R.id.actionButtonIndicator);
        grabButton = (ToggleButton) view.findViewById(R.id.grabButton);

        actionButton = view.findViewById(R.id.actionButton);

        final ToggleButton paletteButton = (ToggleButton) view.findViewById(R.id.paletteButton);
        final Button undoButton = view.findViewById(R.id.undoButton);
        final Button redoButton = view.findViewById(R.id.redoButton);

        tileDrawer = view.findViewById(R.id.tileDrawer);

        GridLayout paletteGridBasic = (GridLayout) view.findViewById(R.id.paletteGridBasic);
        GridLayout paletteGridBackground = (GridLayout) view.findViewById(R.id.paletteGridBackground);
        GridLayout paletteGridTech = (GridLayout) view.findViewById(R.id.paletteGridTech);

        tilePalette = new ArrayList(paletteGridBasic.getChildCount());
        for (int i = 0; i < paletteGridBasic.getChildCount(); i++) {
            tilePalette.add((Button) paletteGridBasic.getChildAt(i));

            final int tileId = i;
            tilePalette.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTile = tileId;
                    paletteButton.setChecked(false);

                    paletteButton.setBackgroundResource(paletteIcons.get("basic "+tileId));
                    Log.d(v.getClass().getName(), "Setting tile to basic "+tileId);
                    if (!ControllerActivity.controllerDebug) {
                        Log.d("MYDEBUG", "sending message");
                        connection.sendMessage("basic " + tileId);
                    }

                }
            });
        }

        tilePalette = new ArrayList(paletteGridBackground.getChildCount());
        for (int i = 0; i < paletteGridBackground.getChildCount(); i++) {
            tilePalette.add((Button) paletteGridBackground.getChildAt(i));

            final int tileId = i;
            tilePalette.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTile = tileId;
                    paletteButton.setChecked(false);

                    paletteButton.setBackgroundResource(paletteIcons.get("bg "+tileId));
                    Log.d(v.getClass().getName(), "Setting tile to bg "+tileId);
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("bg "+tileId);

                }
            });
        }

        tilePalette = new ArrayList(paletteGridTech.getChildCount());
        for (int i = 0; i < paletteGridTech.getChildCount(); i++) {
            tilePalette.add((Button) paletteGridTech.getChildAt(i));

            final int tileId = i;
            tilePalette.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTile = tileId;
                    paletteButton.setChecked(false);

                    paletteButton.setBackgroundResource(paletteIcons.get("tech "+tileId));
                    Log.d(v.getClass().getName(), "Setting tile to tech "+tileId);
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("tech "+tileId);

                }
            });
        }

        pencilButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.INVISIBLE);
                    Log.d("touch", "pencil");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("pencil");
                    eraserButton.setChecked(false);
                    grabButton.setChecked(false);
                    actionButtonIndicator.setImageResource(R.drawable.tx_ui_pencil2);
                } else {
                    Log.d("touch", "pencil_end");
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("pencil_end");
                }
            }
        });

        eraserButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.INVISIBLE);
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("eraser");
                    pencilButton.setChecked(false);
                    grabButton.setChecked(false);
                    actionButtonIndicator.setImageResource(R.drawable.tx_ui_eraser2);
                } else {
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("eraser_end");
                }
            }
        });

        grabButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.INVISIBLE);
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("grab");
                    pencilButton.setChecked(false);
                    eraserButton.setChecked(false);
                    actionButtonIndicator.setImageResource(R.drawable.tx_ui_grab2);
                } else {
                    if (!ControllerActivity.controllerDebug)
                        connection.sendMessage("grab_end");
                }
            }
        });

        paletteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.VISIBLE);
                } else {
                    tileDrawer.setVisibility(View.INVISIBLE);
                }
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("undo");
            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage("redo");
            }
        });

        actionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        actionButton.setPressed(true);
                        Log.d("touch", "action_start");
                        if (!ControllerActivity.controllerDebug)
                            connection.sendMessage("action_start");
                        return true;
                    case MotionEvent.ACTION_UP:
                        actionButton.setPressed(false);
                        Log.d("touch", "action_end");
                        if (!ControllerActivity.controllerDebug)
                            connection.sendMessage("action_end");
                        return true;
                }
                return false;
            }
        });

        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}