package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import cz.msebera.android.httpclient.protocol.HTTP;
/**
 * Created by vlad on 5/20/18.
 */

public class ServerThread extends Thread{
    ServerSocket serverSocket;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    private boolean isRunning;

    //private ServerSocket serverSocket;

    public void startServer() {
        isRunning = true;
        start();
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    Map<String, Timestamp> data = new HashMap<String, Timestamp>();
    int port;
    public ServerThread(int port){
        this.port = port;

    }

    public synchronized void setData(String key, Timestamp value) {
        this.data.put(key, value);
    }

    public synchronized Map<String, Timestamp> getData() {
        return data;
    }
    public synchronized String getValue(String key) {
        if(getData().containsKey(key))
            return getData().get(key).toString() + "\n";

        return "none\n";


    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
    public void stopServer() {
        isRunning = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    Log.v(Constants.TAG, "stopServer() method invoked "+serverSocket);
                } catch(IOException ioException) {
                    Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void run() {

        ArrayList<CommunicationThread> clientThreads = new ArrayList<CommunicationThread>();
        try {
            serverSocket = new ServerSocket(port);

            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this,socket);
                communicationThread.start();

                clientThreads.add(communicationThread);
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}