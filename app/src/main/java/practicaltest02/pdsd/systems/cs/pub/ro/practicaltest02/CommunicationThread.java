package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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

public class CommunicationThread extends Thread{


    Socket clientSocket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    ServerThread serverThread;


    public CommunicationThread(ServerThread serverThread, Socket clientSocket)
    {
        this.serverThread = serverThread;
        this.clientSocket = clientSocket;
    }

       /* @Override
        public void run() {
            try {
                Log.v(Constants.TAG, "Connection opened with "+clientSocket.getInetAddress()+":"+clientSocket.getLocalPort());
                BufferedReader bufferedReader = Utilities.getReader(clientSocket);
                PrintWriter printWriter = Utilities.getWriter(clientSocket);
               // printWriter.println(serverTextEditText.getText().toString());
                clientSocket.close();
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }*/

    public void run() {
        if (clientSocket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(clientSocket);
            PrintWriter printWriter = Utilities.getWriter(clientSocket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            String key = bufferedReader.readLine();
            String value = bufferedReader.readLine();
            if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }


            Map<String, Timestamp> data = serverThread.getData();
            //WeatherForecastInformation weatherForecastInformation = null;

            //daca s a apasat putonul???
            if(key != null && value != null) {
                HttpClient httpClient = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);

                String response = httpClient.execute(httpGet, responseHandler);

                JSONObject content = new JSONObject(response);
                response = content.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction");
                Long time = content.getLong("currentFileTime");
                Timestamp timestamp = new Timestamp(value, 1233211);
                serverThread.setData(key, timestamp);

                printWriter.println(timestamp.getValue());
                printWriter.flush();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } catch (JSONException jsonException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
            if (Constants.DEBUG) {
                jsonException.printStackTrace();
            }
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }


}
