package patchworks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import patchworks.fragments.GameFinderFragment;
import patchworks.R;
import patchworks.fragments.LevelBrowserFragment;
import patchworks.fragments.QueueFragment;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.app.ActionBar mActionBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private int navIndex;
    private String[] activityTitles;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_LEVELS = "levels";
    private static final String TAG_QUEUE = "queue";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    private NavigationView navigationView;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_levels:
//                    load_fragment("levels");
//                    return true;
//                case R.id.navigation_controller:
//                    load_fragment("games");
//                    return true;
//                case R.id.navigation_settings:
//                    return true;
//            }
//            return false;
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navIndex = 0;

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Patchworks");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        drawerLayout.addDrawerListener(mDrawerToggle);
        setupNavigationView();
        loadFragment();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_connect:

                Log.d("TESTS", "clicked action_connect");
                //create alert dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //set title and message
                builder.setTitle("IP Connect");
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.connect_dialog, null);
                builder.setView(dialogView);
                builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText ipField = (EditText) dialogView.findViewById(R.id.ipAddressField);
                        String ipAddr = ipField.getText().toString();
                        if (!ipAddr.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))
                            Toast.makeText(getApplicationContext(), "You did not enter a valid IP address!", Toast.LENGTH_LONG).show();
                        else {
                            openController(ipAddr);
                        }
                    }
                });

                builder.show();


                //openController("172.31.53.248");

                return true;

            case R.id.action_debug:
                backdoorToController();
                return true;

            case R.id.action_search:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // opens controller given IP address
    protected void openController(String address) {
        Intent intent = new Intent(this, ControllerActivity.class);
        intent.putExtra("ip",  address);
        startActivity(intent);
    }

    // opens controller activity without connecting via IP
    protected void backdoorToController() {
        Intent intent = new Intent(this, ControllerActivity.class);
        intent.putExtra("debug", "debug");
        startActivity(intent);
    }

    private void loadFragment() {
        navigationView.getMenu().getItem(navIndex).setChecked(true);
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = getFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        //        android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_space, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        drawerLayout.closeDrawers();

    }

    private Fragment getFragment() {
        switch (navIndex) {
            case 0:
                // home
                GameFinderFragment gameFinderFragment = new GameFinderFragment();
                return gameFinderFragment;
            case 1:
                // levels
                LevelBrowserFragment levelBrowserFragment = new LevelBrowserFragment();
                return levelBrowserFragment;
            case 2:
                // queue
                QueueFragment queueFragment = new QueueFragment();
                return queueFragment;
            default:
                return new GameFinderFragment();
        }
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_levels:
                        navIndex = 1;
                        CURRENT_TAG = TAG_LEVELS;
                        break;
                    case R.id.nav_queue:
                        navIndex = 2;
                        CURRENT_TAG = TAG_QUEUE;
                        break;
                    case R.id.nav_notifications:
                        navIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    default:
                        navIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment();

                return true;
            }
        });

    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navIndex]);
    }

}
