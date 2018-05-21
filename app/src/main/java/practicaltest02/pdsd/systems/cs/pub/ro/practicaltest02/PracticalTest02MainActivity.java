package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    ServerThread serverThread;
    EditText serverPortEditText;// = (EditText) findViewById(R.id.server_port_edit_text);
    Button connect;// = (Button) findViewById(R.id.connect_button);
    TextView infor;

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
          /*  if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }*/
            serverThread.start();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = findViewById(R.id.server_port_edit_text);
        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(new ConnectButtonClickListener());

        infor = findViewById(R.id.info);

        final View cheie = findViewById(R.id.client_key);
        final View value = findViewById(R.id.client_value);
        final View cheie2 = findViewById(R.id.client_key2);

        View put = findViewById(R.id.put);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = Constants.WEB_SERVICE_ADDRESS;
                int port = Integer.parseInt(((EditText)serverPortEditText).getText().toString());

                String key = ((EditText)cheie).getText().toString();
                String value2 = ((EditText)value).getText().toString();
                ClientThread clientThread = new ClientThread(ipAddress, port, infor, key, value2, null);
                clientThread.start();
            }
        });

        View get = findViewById(R.id.get);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int port = Integer.parseInt(((EditText)serverPortEditText).getText().toString());
                String cheievalue = ((EditText)cheie2).getText().toString();
                ClientThread clientThread = new ClientThread(Constants.WEB_SERVICE_ADDRESS, port, infor, null, null,cheievalue);
                clientThread.start();
            }
        });
    }
    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}
