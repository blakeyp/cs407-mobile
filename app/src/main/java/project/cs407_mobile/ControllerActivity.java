package project.cs407_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static project.cs407_mobile.MainActivity.DEBUG_TAG;

public class ControllerActivity extends AppCompatActivity {

    private ConnectionService connectionService;
    public Button buttonJump;
    public Button buttonDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Intent intent = getIntent();

        String ipAddr = intent.getStringExtra("ip");   // get input IP address

        connectionService = new ConnectionService();
        connectionService.connectToIP(ipAddr, this);

        setConnectStatus("Connecting to " + ipAddr + " ...");

        buttonJump = (Button) findViewById(R.id.buttonJump);   // reference to the disconnect button

        // button press event listener
        buttonJump.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Pressed Jump button");
                connectionService.sendMessage("jump");
            }
        });

        buttonJump.setClickable(false);
        buttonJump.setAlpha(.5f);

        buttonDisconnect = (Button) findViewById(R.id.buttonDisconnect);   // reference to the disconnect button

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("DEBUG", "Pressed Disconnect button");
                connectionService.closeConnection();
            }
        });

        buttonDisconnect.setClickable(false);
        buttonDisconnect.setAlpha(.5f);
    }

    protected void setConnectStatus(String message) {
        TextView ipPlaceholder = (TextView) findViewById(R.id.ipAddress);
        ipPlaceholder.setText(message);   // fill IP address placeholder with current connection status
    }

    @Override
    protected void onDestroy() {
        connectionService.closeConnection();
        super.onDestroy();
    }

}
