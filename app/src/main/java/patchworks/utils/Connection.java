package patchworks.utils;

import android.app.Activity;
import android.os.AsyncTask;
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

import static patchworks.activities.LoginActivity.DEBUG_TAG;

public class Connection {

    private Activity controller;
    private MakeConnection connection;

    private PrintWriter printwriter;

    public void debug() {
        Log.d(DEBUG_TAG, "Fragment has connection");
    }

    public void connectToIP(String ipAddr, Activity ctrl) {
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
                    publishProgress(message);
                }
            } catch (IOException ex) {}

            Log.d(DEBUG_TAG, "finished listening!!");

            return null;
        }

        protected void onProgressUpdate(String... values) {
//            if (values[0].equals("capture")) {
//                Button captureButton = controller.findViewById(R.id.captureButton);
//                Log.d(DEBUG_TAG, "going going gone");
//                //LevelRuntimeFragment.captureButton.setVisibility(View.GONE);
//                captureButton.setBackgroundResource(R.drawable.tx_capture);
//                captureButton.setEnabled(true);
//            }

            Button captureButton = controller.findViewById(R.id.captureButton);
            Log.d(DEBUG_TAG, values[0]);

            switch (values[0]) {
                case "ufo_on":
                    captureButton.setBackgroundResource(R.drawable.tx_capture);
                    captureButton.setEnabled(true);
                    LevelRuntimeFragment.captured = "ufo";
                    break;
                case "slime_on":
                    captureButton.setBackgroundResource(R.drawable.tx_capture);
                    captureButton.setEnabled(true);
                    LevelRuntimeFragment.captured = "slime";
                    break;
                case "capture_off":
                    captureButton.setBackgroundResource(R.drawable.tx_capture_off);
                    captureButton.setEnabled(false);
                    break;
            }

        }

    }

}
