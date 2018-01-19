package project.cs407_mobile;

//import android.app.ActionBar;
import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String DEBUG_TAG = "MYDEBUG";

    private EditText ipField;
    private Button buttonConnect;
    private Button buttonDebug;   // button to debug controller
    private Button buttonLevels;   // button to debug controller

    private android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   // on opening the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mActionBar.hide();   // hide action bar

        //System.loadLibrary("cs407_server");

        ipField = (EditText) findViewById(R.id.editText);   // reference to IP address text field
        buttonConnect = (Button) findViewById(R.id.buttonConnect);   // reference to connect button
        buttonDebug = (Button) findViewById(R.id.buttonDebug);   // reference to debug button
        buttonLevels = (Button) findViewById(R.id.buttonLevels);   // reference to levels button

        // on clicking connect button open controller with input IP address
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Pressed Connect button");
                String ipAddr = ipField.getText().toString();
                if (!ipAddr.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))
                    Toast.makeText(getApplicationContext(), "You did not enter a valid IP address!", Toast.LENGTH_LONG).show();
                else {
                    Log.d(DEBUG_TAG, "IP address: " + ipAddr);
                    openController(ipAddr);
                }
            }
        });

        // on clicking debug button open controller to debug without connecting
        buttonDebug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backdoorToController();
            }
        });

        // on clicking levels button open level browser ui (currently incomplete)
        buttonLevels.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLevelBrowser();
            }
        });

    }

    // opens controller given IP address
    protected void openController(String address) {
        Log.d(DEBUG_TAG, "Connect Controller Activity");
        Intent intent = new Intent(this, ControllerActivity.class);
        intent.putExtra("ip",  address);
        startActivity(intent);
    }

    // opens controller given IP address
    protected void openLevelBrowser() {
        Log.d(DEBUG_TAG, "Level Browser Activity");
        Intent intent = new Intent(this, LevelBrowserActivity.class);
        startActivity(intent);
    }

    // opens controller activity without connecting via IP
    protected void backdoorToController() {
        Log.d(DEBUG_TAG, "Debug Controller Activity");
        Intent intent = new Intent(this, ControllerActivity.class);
        startActivity(intent);
    }

}
