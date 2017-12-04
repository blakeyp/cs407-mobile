package project.cs407_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static project.cs407_mobile.MainActivity.DEBUG_TAG;

public class ControllerActivity extends AppCompatActivity {

    private ConnectionService connectionService;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    };
    //private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private TouchPad touchPad;

    private ToggleButton eraserButton;
    private ToggleButton pencilButton;

    private ScrollView tileDrawer;

    private HashMap<Integer, Integer> paletteIcons;

    private int mSelectedTile = 1;

    private ArrayList<Button> tilePalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);

        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();

        paletteIcons = new HashMap();
        paletteIcons.put(0, R.drawable.tx_tile_solid);
        paletteIcons.put(1, R.drawable.tx_tile_semisolid);
        paletteIcons.put(2, R.drawable.tx_tile_ufo);
        paletteIcons.put(3, R.drawable.tx_tile_bush_01);
        paletteIcons.put(4, R.drawable.tx_tile_bush_02);
        paletteIcons.put(5, R.drawable.tx_tile_cloud_01);
        paletteIcons.put(6, R.drawable.tx_tile_cloud_02);
        paletteIcons.put(7, R.drawable.tx_tile_mountain);


        if (intent.hasExtra("ip")) {

            String ipAddr = intent.getStringExtra("ip");   // get input IP address

            connectionService = new ConnectionService();
            connectionService.connectToIP(ipAddr, this);

            touchPad = (TouchPad) findViewById(R.id.touchPad);
            touchPad.setDetector(new GestureDetector(touchPad.getContext(), new scrollListener(touchPad)));

        }

        eraserButton = (ToggleButton) findViewById(R.id.eraserButton);
        pencilButton = (ToggleButton) findViewById(R.id.pencilButton);
        final ToggleButton paletteButton = (ToggleButton) findViewById(R.id.paletteButton);
        final Button undoButton = (Button) findViewById(R.id.undoButton);
        final Button redoButton = (Button) findViewById(R.id.redoButton);

        tileDrawer = (ScrollView) findViewById(R.id.tileDrawer);

        GridLayout paletteGridBasic = (GridLayout) findViewById(R.id.paletteGridBasic);
        GridLayout paletteGridBackground = (GridLayout) findViewById(R.id.paletteGridBackground);
        GridLayout paletteGridTech = (GridLayout) findViewById(R.id.paletteGridTech);

        tilePalette = new ArrayList(paletteGridBasic.getChildCount());
        for (int i = 0; i < paletteGridBasic.getChildCount(); i++) {
            tilePalette.add((Button) paletteGridBasic.getChildAt(i));

            final int tileId = i;
            tilePalette.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTile = tileId;
                    paletteButton.setChecked(false);

                    paletteButton.setBackgroundResource(paletteIcons.get(tileId));
                    Log.d(v.getClass().getName(), "Setting tile to id "+tileId);
                    connectionService.sendMessage("tile "+tileId);

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

//        ColorStateList p = new ColorStateList(
//                new int[][]{
//                        new int[]{android.R.attr.state_checked},//1
//                        new int[]{-android.R.attr.state_checked} //1
//                },
//                new int[] {
//                        Color.BLUE, //1
//                        Color.WHITE
//                });
//
//        eraserButton.setBackgroundTintList(p);
//        eraserButton.invalidate();
//        pencilButton.setBackgroundTintList(p);
//        pencilButton.invalidate();



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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



}
