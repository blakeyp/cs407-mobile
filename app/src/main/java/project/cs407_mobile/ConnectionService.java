package project.cs407_mobile;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import static project.cs407_mobile.MainActivity.DEBUG_TAG;

class ConnectionService {

    private ControllerActivity controller;
    private MakeConnection connection;

    private PrintWriter printwriter;


    void connectToIP(String ipAddr, ControllerActivity ctrl) {
        controller = ctrl;
        connection = new MakeConnection(ipAddr);
        connection.execute();
    }

    void sendMessage(String message) {
        SendMessage newMessage = new SendMessage(message);
        newMessage.execute();
    }

    void closeConnection() {
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
            controller.setConnectStatus("Connected to: " + ipAddr);
            controller.buttonJump.setClickable(true);
            controller.buttonJump.setAlpha(1);
            controller.buttonDisconnect.setClickable(true);
            controller.buttonDisconnect.setAlpha(1);
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

}
