package patchworks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;

import patchworks.fragments.GameFinderFragment;
import patchworks.fragments.LevelEditorFragment;
import patchworks.fragments.LevelRuntimeFragment;
import patchworks.utils.Game;
import patchworks.adapters.GameAdapter;
import patchworks.R;

public class MainActivity extends AppCompatActivity
        implements GameFinderFragment.OnFragmentInteractionListener{

    private android.support.v7.app.ActionBar mActionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_levels:
                    return true;
                case R.id.navigation_controller:
                    load_fragment();
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_browser);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_levels);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Patchworks");
        mActionBar.setDisplayHomeAsUpEnabled(false);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect:
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
                return true;

            case R.id.action_debug:
                backdoorToController();
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

    private void load_fragment() {
        // load initial LevelEditorFragment fragment (default controller)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new GameFinderFragment();

        // overlay fragment onto activity
        fragmentTransaction.replace(R.id.fragment_space, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
