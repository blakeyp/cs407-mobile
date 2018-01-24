package project.cs407_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;

import project.cs407_mobile.utils.Game;
import project.cs407_mobile.adapters.GameAdapter;
import project.cs407_mobile.R;

public class LevelBrowserActivity extends AppCompatActivity {

    private android.support.v7.app.ActionBar mActionBar;

    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private ArrayList<Game> gameList;


    private Button mIPButton;
    private EditText mIPField;

    private Button buttonDebug;   // button to debug controller

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_levels:
                    return true;
                case R.id.navigation_controller:
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
        navigation.setSelectedItemId(R.id.navigation_controller);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Patchworks");
        mActionBar.setDisplayHomeAsUpEnabled(false);

        buttonDebug = (Button) findViewById(R.id.buttonDebug);   // reference to debug button

        // on clicking debug button open controller to debug without connecting
        buttonDebug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backdoorToController();
            }
        });

        mIPButton = (Button) findViewById(R.id.ButtonConnect);
        mIPField = (EditText) findViewById(R.id.ipAddressField);

        mIPButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ipAddr = mIPField.getText().toString();
                if (!ipAddr.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))
                    Toast.makeText(getApplicationContext(), "You did not enter a valid IP address!", Toast.LENGTH_LONG).show();
                else {
                    openController(ipAddr);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.gamesRecyclerView);

        gameList = new ArrayList<>();
        adapter = new GameAdapter(this, gameList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        try {
            InetAddress netAddress = InetAddress.getByName("127.0.0.1");
            gameList.add(new Game(netAddress, "Game Name", "state", 2, true));
        } catch (java.net.UnknownHostException e) {
            Log.d("fuck ", "something bad happened");
        }

        adapter.notifyDataSetChanged();

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

}
