package patchworks.utils;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import patchworks.R;
import patchworks.fragments.LevelRuntimeFragment;
import patchworks.fragments.SlimeFragment;
import patchworks.fragments.UFOFragment;

import static patchworks.activities.LoginActivity.DEBUG_TAG;

public class Connection {

    private AppCompatActivity controller;
    private MakeConnection connection;

    private PrintWriter printwriter;

    public void debug() {
        Log.d(DEBUG_TAG, "Fragment has connection");
    }

    public void connectToIP(String ipAddr, AppCompatActivity ctrl) {
        controller = ctrl;
        connection = new MakeConnection(ipAddr);
        connection.execute();
    }

    public void sendMessage(String message) {
        Log.d(DEBUG_TAG, "TRYING TO SEND A MESSAGE");
        SendMessage newMessage = new SendMessage(message);
        newMessage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);   // run in parallel with other tasks
    }

    public void closeConnection() {
        Log.d(DEBUG_TAG, "Closing stuff");
        if (printwriter != null) {
            printwriter.close();
            try {
                connection.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(DEBUG_TAG, "Closed stuff");
    }



    private class MakeConnection extends AsyncTask<Void, Void, Void> {

        private String ipAddr;
        private Socket client;

        MakeConnection(String ipAddr) {
            this.ipAddr = ipAddr;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d(DEBUG_TAG, "Waiting for connection...");
                client = new Socket();
                client.connect(new InetSocketAddress(ipAddr, 9000), 10000);   // set timeout for connection blocking
                Log.d(DEBUG_TAG, "Connection made");
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.println("connected");   // write message to output stream with EOL char
                printwriter.flush();

            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Connection refused!!!");
                cancel(true);   // cancel this AsyncTask
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Log.d(DEBUG_TAG, "Finished connecting");
            // now listen for messages coming back
            Listen listen = new Listen(client);
            listen.execute();
        }

        protected void onCancelled(Void result) {
            Toast.makeText(controller, "Connection failed! Please try again.", Toast.LENGTH_LONG).show();
            controller.finish();   // close controller activity
        }

    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {

        private String message;

        SendMessage(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(DEBUG_TAG, "Sending message");
            printwriter.println(message);
            printwriter.flush();
            Log.d(DEBUG_TAG, "Sent message");
            return null;
        }

    }


    // for receiving messages
    private class Listen extends AsyncTask<Void, String, Void> {

        private Socket client;

        Listen(Socket client) { this.client = client; }

        @Override
        protected Void doInBackground(Void... params) {

            String message;

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((message = in.readLine()) != null) {
                    Log.d("readr", "Incoming message: "+message);
                    publishProgress(message);   // submit message to be handled
                }
            } catch (IOException ex) {}

            Log.d(DEBUG_TAG, "finished listening!!");

            return null;
        }

        // handle incoming messages on the UI thread
        protected void onProgressUpdate(String... values) {

            String message = values[0];   // assume only one message at a time
            Log.d(DEBUG_TAG, message);

            Button captureButton = (Button) controller.findViewById(R.id.captureButton);

            switch (message) {

                case "pobj_UFO":
                    captureButton.setBackgroundResource(R.drawable.tx_capture);
                    captureButton.setEnabled(true);
                    LevelRuntimeFragment.captured = "ufo";
                    break;
                case "pobj_Slime":
                    captureButton.setBackgroundResource(R.drawable.tx_capture);
                    captureButton.setEnabled(true);
                    LevelRuntimeFragment.captured = "slime";
                    break;
                case "pobj_SpikeTrap":
                    captureButton.setBackgroundResource(R.drawable.tx_capture);
                    captureButton.setEnabled(true);
                    LevelRuntimeFragment.captured = "spike";
                    break;
                case "capture_off":
                    captureButton.setBackgroundResource(R.drawable.tx_capture_off);
                    captureButton.setEnabled(false);
                    break;

                case "runtime":   // switch from level editor to runtime controller
                    FragmentTransaction transaction = controller.getSupportFragmentManager().beginTransaction();
                    Fragment fragment = new LevelRuntimeFragment();
                    transaction.replace(R.id.fullscreen_content, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;

            }

        }

    }

}
