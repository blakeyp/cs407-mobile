package project.cs407_mobile;

import android.content.Context;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import static project.cs407_mobile.LoginActivity.DEBUG_TAG;

public class LevelEditor extends Fragment {

    /****/

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LevelEditor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelEditor.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelEditor newInstance(String param1, String param2) {
        LevelEditor fragment = new LevelEditor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /****/


    private ConnectionService connectionService;
    private TouchPad touchPad;
    private ToggleButton eraserButton;
    private ToggleButton pencilButton;
    private ScrollView tileDrawer;
    private HashMap<String, Integer> paletteIcons;
    private ArrayList<Button> tilePalette;
    private int mSelectedTile = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // retrieve established connection from activity
        connectionService = ((ControllerActivity)getActivity()).getConnectionService();
        connectionService.debug();

    }

    class scrollListener extends GestureDetector.SimpleOnGestureListener {

        TouchPad mView;

        public scrollListener(TouchPad t) {
            mView = t;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {

            Log.d(DEBUG_TAG, dx/mView.getWidth() + "," + dy/mView.getHeight());
            connectionService.sendMessage(dx/mView.getWidth() + "," +dy/mView.getHeight() );

            mView.offsetX = Math.round((mView.offsetX - dx)%mView.mPattern.getWidth());
            mView.offsetY = Math.round((mView.offsetY - dy)%mView.mPattern.getHeight());
            mView.invalidate();

            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.level_editor, container, false);

        paletteIcons = new HashMap();
        paletteIcons.put("basic 0", R.drawable.tx_tile_solid);
        paletteIcons.put("basic 1", R.drawable.tx_tile_semisolid);
        paletteIcons.put("basic 2", R.drawable.tx_tile_ladder);
        paletteIcons.put("basic 3", R.drawable.tx_tile_crate);
        paletteIcons.put("bg 0", R.drawable.tx_tile_bush_01);
        paletteIcons.put("bg 1", R.drawable.tx_tile_bush_02);
        paletteIcons.put("bg 2", R.drawable.tx_tile_cloud_01);
        paletteIcons.put("bg 3", R.drawable.tx_tile_cloud_02);
        paletteIcons.put("bg 4", R.drawable.tx_tile_mountain);
        paletteIcons.put("tech 0", R.drawable.tx_tile_startpoint);
        paletteIcons.put("misc 0", R.drawable.tx_tile_doughnut);
        paletteIcons.put("misc 1", R.drawable.tx_tile_ufo);

        touchPad = (TouchPad) view.findViewById(R.id.touchPad);
        touchPad.setDetector(new GestureDetector(touchPad.getContext(), new scrollListener(touchPad)));

        eraserButton = (ToggleButton) view.findViewById(R.id.eraserButton);
        pencilButton = (ToggleButton) view.findViewById(R.id.pencilButton);

        final ToggleButton paletteButton = (ToggleButton) view.findViewById(R.id.paletteButton);
        final Button undoButton = (Button) view.findViewById(R.id.undoButton);
        final Button redoButton = (Button) view.findViewById(R.id.redoButton);

        tileDrawer = (ScrollView) view.findViewById(R.id.tileDrawer);

        GridLayout paletteGridBasic = (GridLayout) view.findViewById(R.id.paletteGridBasic);
        GridLayout paletteGridBackground = (GridLayout) view.findViewById(R.id.paletteGridBackground);
        GridLayout paletteGridTech = (GridLayout) view.findViewById(R.id.paletteGridTech);
        GridLayout paletteGridMisc = (GridLayout) view.findViewById(R.id.paletteGridMisc);

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
                    connectionService.sendMessage("basic "+tileId);

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
                    connectionService.sendMessage("bg "+tileId);

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
                    connectionService.sendMessage("tech "+tileId);

                }
            });
        }

        tilePalette = new ArrayList(paletteGridMisc.getChildCount());
        for (int i = 0; i < paletteGridMisc.getChildCount(); i++) {
            tilePalette.add((Button) paletteGridMisc.getChildAt(i));

            final int tileId = i;
            tilePalette.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTile = tileId;
                    paletteButton.setChecked(false);

                    paletteButton.setBackgroundResource(paletteIcons.get("misc "+tileId));
                    Log.d(v.getClass().getName(), "Setting tile to misc "+tileId);
                    connectionService.sendMessage("misc "+tileId);

                }
            });
        }

        pencilButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.INVISIBLE);
                    connectionService.sendMessage("pencil");
                    eraserButton.setChecked(false);
                } else {
                    connectionService.sendMessage("pencil_end");
                }
            }
        });

        eraserButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDrawer.setVisibility(View.INVISIBLE);
                    connectionService.sendMessage("eraser");
                    pencilButton.setChecked(false);
                } else {
                    connectionService.sendMessage("eraser_end");
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
                connectionService.sendMessage("undo");
            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionService.sendMessage("redo");
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

}