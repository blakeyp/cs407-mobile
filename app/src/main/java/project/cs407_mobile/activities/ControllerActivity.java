package project.cs407_mobile.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import project.cs407_mobile.fragments.LevelEditorFragment;
import project.cs407_mobile.utils.Connection;
import project.cs407_mobile.R;

public class ControllerActivity extends AppCompatActivity
        implements LevelEditorFragment.OnFragmentInteractionListener {

    private View mContentView;
    private Connection connection;

    // for hiding UI elements/action bar for fullscreen mode
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    public static boolean controllerDebug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);
        mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();

        if ((intent.hasExtra("ip")) || (intent.hasExtra("debug"))) {

            if (intent.hasExtra("ip")) {
                String ipAddr = intent.getStringExtra("ip");   // get input IP address
                connection = new Connection();
                connection.connectToIP(ipAddr, this);   // establish connection
            }
            else if (intent.hasExtra("debug")) {
                controllerDebug = true;
            }

            // load initial LevelEditorFragment fragment (default controller)
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LevelEditorFragment fragment = new LevelEditorFragment();

            // overlay fragment onto activity
            fragmentTransaction.add(R.id.fullscreen_content, fragment);
            fragmentTransaction.commit();

        }

    }

    // allow fragments to get established connection
    public Connection getConnection() {
        return connection;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}   // for handling fragments


    // for hiding UI elements/action bar for fullscreen mode
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {   // re-hide action/nav bar after e.g. waking device
            delayedHide(300);
        } else {
            mHideHandler.removeMessages(0);
        }
    }

}
