package project.cs407_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;

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

    private ArrayList<ToggleButton> tilePalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);

        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();

        if (intent.hasExtra("ip")) {

            String ipAddr = intent.getStringExtra("ip");   // get input IP address

            connectionService = new ConnectionService();
            connectionService.connectToIP(ipAddr, this);

            touchPad = (TouchPad) findViewById(R.id.touchPad);
            touchPad.setDetector(new GestureDetector(touchPad.getContext(), new scrollListener(touchPad)));

        }

        eraserButton = (ToggleButton) findViewById(R.id.eraserButton);
        pencilButton = (ToggleButton) findViewById(R.id.pencilButton);

        GridLayout paletteGrid = (GridLayout) findViewById(R.id.paletteGrid);

        tilePalette = new ArrayList(paletteGrid.getChildCount());
        for (int i = 0; i < paletteGrid.getChildCount(); i++) {
            tilePalette.add((ToggleButton) paletteGrid.getChildAt(i));
            //Log.d(this.getClass().getName(), tilePalette.get(i).toString());
            tilePalette.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setAlpha(1);
                        Log.d(buttonView.getClass().getName(), "checked");
                    } else {
                        buttonView.setAlpha(0.5f);
                        Log.d(buttonView.getClass().getName(), "not checked");
                    }
                }
            });
        }

//        eraserButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//
//                }
//            }
//        }

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

        View mView;

        public scrollListener(View v) {
            mView = v;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {
            Log.d(DEBUG_TAG, dx/mView.getWidth() + "," + dy/mView.getHeight());
            connectionService.sendMessage(dx/mView.getWidth() + "," +dy/mView.getHeight() );
            return true;
        }
    }



}
