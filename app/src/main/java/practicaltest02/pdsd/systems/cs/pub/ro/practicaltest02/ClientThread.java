package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

//import static practicaltest02.eim.systems.cs.pub.ro.examen.R.string.city;

/**
 * Created by vlad on 5/21/18.
 */

public class ClientThread extends Thread {

    String ipAddress;
    int port;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    View infoView;
    String city;
    // View informationType;
    Button put;
    Button get;
    String key;
    String value;
    String key2;

    public ClientThread(String ipAddress, int port, View info, String key, String value, String key2) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.infoView = info;
        this.key = key;
        this.value = value;
        this.key2 = key2;

    }

    public ClientThread(String ipAddress, int port, View info, Button put, Button get) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.infoView = info;

        this.city = city;
        this.put = put;
        this.get = get;
    }

    @Override
    public void run() {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);

            String response = httpClient.execute(httpGet, responseHandler);

            JSONObject content = new JSONObject(response);
            response = content.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction");
            Long time = content.getLong("currentFileTime");
            Timestamp timestamp = new Timestamp(value, 1233211);
            //serverThread.setData(key, timestamp);

            printWriter.println(timestamp.getValue());
            printWriter.flush();

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
        }
    }
}
