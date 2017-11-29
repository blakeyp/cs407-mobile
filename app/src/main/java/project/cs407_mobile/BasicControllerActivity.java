package project.cs407_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static project.cs407_mobile.MainActivity.DEBUG_TAG;

public class BasicControllerActivity extends AppCompatActivity {

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

    private Button ctrlLeft;
    private Button ctrlRight;
    private Button ctrlShoot;
    private TouchPad touchPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_controller);

        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();
        String ipAddr = intent.getStringExtra("ip");   // get input IP address

        connectionService = new ConnectionService();
        connectionService.connectToIP(ipAddr, this);

        ctrlLeft = (Button) findViewById(R.id.ctrlLeft);
        ctrlRight = (Button) findViewById(R.id.ctrlRight);
        ctrlShoot = (Button) findViewById(R.id.ctrlShoot);
        touchPad = (TouchPad) findViewById(R.id.touchPad);

        touchPad.setDetector(new GestureDetector(touchPad.getContext(), new scrollListener()));

        // button press event listener
        ctrlLeft.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        Log.d(DEBUG_TAG, "Pressed Left button");
                        connectionService.sendMessage("left");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        Log.d(DEBUG_TAG, "Released Left button");
                        connectionService.sendMessage("leftr");
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        // button press event listener
        ctrlRight.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        Log.d(DEBUG_TAG, "Pressed right button");
                        connectionService.sendMessage("right");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        Log.d(DEBUG_TAG, "Released right button");
                        connectionService.sendMessage("rightr");
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        // button press event listener
        ctrlShoot.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        Log.d(DEBUG_TAG, "Pressed shoot button");
                        connectionService.sendMessage("fire");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        Log.d(DEBUG_TAG, "Released shoot button");
                        connectionService.sendMessage("firer");
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
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
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {
            Log.d(DEBUG_TAG, dx + "," + dy);
            connectionService.sendMessage(dx + "," +dy );
            return true;
        }
    }



}
