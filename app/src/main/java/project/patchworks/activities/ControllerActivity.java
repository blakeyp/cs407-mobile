package project.patchworks.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import project.patchworks.fragments.LevelEditorFragment;
import project.patchworks.fragments.LevelRuntimeFragment;
import project.patchworks.fragments.SpikeTrapFragment;
import project.patchworks.utils.Connection;
import project.patchworks.R;

import static project.patchworks.activities.LoginActivity.DEBUG_TAG;

public class ControllerActivity extends AppCompatActivity
        implements LevelEditorFragment.OnFragmentInteractionListener, LevelRuntimeFragment.OnFragmentInteractionListener, SpikeTrapFragment.OnFragmentInteractionListener {

    private View mContentView;
    private Connection connection;

    private Button menuButton;

    // for hiding UI elements/action bar for fullscreen mode
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    public static boolean controllerDebug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);
        mContentView = findViewById(R.id.fullscreen_content);

        menuButton = (Button) findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "clicked menu button shock horror");
            }
        });

        Intent intent = getIntent();

        if ((intent.hasExtra("ip")) || (intent.hasExtra("debug"))) {

            if (intent.hasExtra("ip")) {
                String ipAddr = intent.getStringExtra("ip");   // get input IP address
                load_fragment("editor");
                connection = new Connection();
                connection.connectToIP(ipAddr, this);   // establish connection
            }
            else if (intent.hasExtra("debug")) {
                controllerDebug = true;

                CharSequence controllers[] = new CharSequence[] {"Level Editor", "Level Runtime"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose controller to debug");
                builder.setItems(controllers, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                load_fragment("editor"); break;
                            case 1:
                                load_fragment("runtime");
                        }
                    }
                });
                builder.show();

            }

        }

    }

    private void load_fragment(String controller) {
        // load initial LevelEditorFragment fragment (default controller)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new LevelEditorFragment();

        if (controller.equals("runtime"))
            fragment = new LevelRuntimeFragment();

        // overlay fragment onto activity
        fragmentTransaction.add(R.id.fullscreen_content, fragment);
        fragmentTransaction.commit();
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