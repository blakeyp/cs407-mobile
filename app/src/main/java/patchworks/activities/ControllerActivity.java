package patchworks.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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


import it.sephiroth.android.library.tooltip.Tooltip;
import patchworks.fragments.LevelEditorFragment;
import patchworks.fragments.LevelRuntimeFragment;
import patchworks.fragments.SlimeFragment;
import patchworks.fragments.SpikeFragment;
import patchworks.fragments.UFOFragment;
import patchworks.utils.Connection;
import patchworks.R;

import static patchworks.activities.LoginActivity.DEBUG_TAG;

public class ControllerActivity extends AppCompatActivity {

    private View mContentView;
    private Connection connection;

    private Button menuButton;

    public static Button backButton;
    public static Button helpButton;

    private String helpText = "Default help text";
    private it.sephiroth.android.library.tooltip.Tooltip.Gravity helpGravity = Tooltip.Gravity.RIGHT;
    private int helpWidth = 1500;

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

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "clicked back button shock horror");

                String leave_msg = "leave_";
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fullscreen_content);
                if (f instanceof UFOFragment)
                    leave_msg += "ufo";
                else if (f instanceof SlimeFragment)
                    leave_msg += "slime";
                else if (f instanceof SpikeFragment)
                    leave_msg += "spike";

                if (!ControllerActivity.controllerDebug)
                    connection.sendMessage(leave_msg);

                onBackPressed();
            }
        });

        helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fullscreen_content);
                if (f instanceof LevelRuntimeFragment) {
                    helpText = "Hover over an object and press the Capture Button to take control!";
                    helpGravity = Tooltip.Gravity.BOTTOM;
                    helpWidth = 850;
                }
                else if (f instanceof UFOFragment) {
                    helpText = "Tilt your device to move the UFO and tap anywhere to fire!";
                    helpGravity = Tooltip.Gravity.RIGHT;
                    helpWidth = 1500;
                }
                else if (f instanceof SlimeFragment) {
                    helpText = "Press left/right to move and shake your device for turbo!";
                    helpGravity = Tooltip.Gravity.RIGHT;
                    helpWidth = 1500;
                }
                else if (f instanceof SpikeFragment) {
                    helpText = "Tap anywhere to activate the spike trap!";
                    helpGravity = Tooltip.Gravity.RIGHT;
                    helpWidth = 1500;
                }
                else if (f instanceof LevelEditorFragment) {
                    helpText = "Choose a tile and tool, then draw using the green action button!";
                    helpGravity = Tooltip.Gravity.RIGHT;
                    helpWidth = 1500;
                }

                Tooltip.make(ControllerActivity.this,
                        new Tooltip.Builder(101)
                                .anchor(helpButton, helpGravity)
                                .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, true)
                                    .outsidePolicy(true, true), 0)
                                .text(helpText)
                                .maxWidth(helpWidth)
                                .withArrow(true)
                                .withOverlay(false)
                                //.typeface(mYourCustomFont)
                                .withStyleId(R.style.TooltipStyle)
                                .build()
                ).show();
            }
        });

        Intent intent = getIntent();

        if ((intent.hasExtra("ip")) || (intent.hasExtra("debug"))) {

            if (intent.hasExtra("ip")) {
                String ipAddr = intent.getStringExtra("ip");   // get input IP address
                //load_fragment("editor");

                connection = new Connection();
                connection.connectToIP(ipAddr, this);   // establish connection

                load_fragment("runtime");

//                CharSequence controllers[] = new CharSequence[] {"Level Editor", "Level Runtime"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Choose game mode");
//                builder.setItems(controllers, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                load_fragment("editor"); break;
//                            case 1:
//                                load_fragment("runtime");
//                        }
//                    }
//                });
//                builder.show();

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new LevelEditorFragment();   // default

        switch (controller) {
            case "runtime":
                fragment = new LevelRuntimeFragment(); break;
        }

        // overlay fragment onto activity
        fragmentTransaction.add(R.id.fullscreen_content, fragment, controller);
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
    public void onDestroy() {
        super.onDestroy();
    }


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
            mHideHandler.removeCallbacks(mHideRunnable);
        }
    }

}
