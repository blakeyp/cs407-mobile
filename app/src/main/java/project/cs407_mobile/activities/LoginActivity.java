package project.cs407_mobile.activities;

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

import project.cs407_mobile.R;
import project.cs407_mobile.activities.LevelBrowserActivity;

public class LoginActivity extends AppCompatActivity {

    public static String DEBUG_TAG = "MYDEBUG";

    private EditText usernameField;
    private EditText passwordField;
    private Button buttonLogin;
    private Button buttonGuest;


    private android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   // on opening the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActionBar = getSupportActionBar();
        mActionBar.hide();   // hide action bar

        //System.loadLibrary("cs407_server");

        usernameField = (EditText) findViewById(R.id.usernameField);   // reference to username text field
        passwordField = (EditText) findViewById(R.id.passwordField);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGuest = (Button) findViewById(R.id.buttonGuest);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Pressed Connect button");
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                //authentication shenanigans will happen
                login();
            }
        });

        buttonGuest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to next screen without logging in
                login();
            }
        });

        // on clicking debug button open controller to debug without connecting
//        buttonDebug.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                backdoorToController();
//            }
//        });

    }

    // opens controller given IP address
    protected void login() {
        Intent intent = new Intent(this, LevelBrowserActivity.class);
        startActivity(intent);
        finish();
    }

}