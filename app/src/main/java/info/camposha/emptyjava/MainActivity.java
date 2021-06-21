package info.camposha.emptyjava;
  
import android.content.Intent;  
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button; 
import info.camposha.emptyjava.MyApplication; 

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{  
    Button buttonStart, buttonStop,buttonNext; 
    private Socket mSocket;
    
    Boolean isConnected;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        buttonStart = findViewById(R.id.buttonStart);  
        buttonStop = findViewById(R.id.buttonStop);  
        buttonNext =  findViewById(R.id.buttonNext);  
  
        buttonStart.setOnClickListener(this);  
        buttonStop.setOnClickListener(this);  
        buttonNext.setOnClickListener(this);  

        try {
            mSocket = IO.socket("https://3000-black-eel-ugbjs2z7.ws-us09.gitpod.io");
            mSocket.connect();
            mSocket.emit("join", "Nickname");
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        // MyApplication app = (MyApplication) getApplication();
        // mSocket = app.getSocket();
        // mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        mSocket.on("newLink", onNewLink);      
  
    }  

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // mSocket.off("login", onLogin);
    }

   

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {                       
                        mSocket.emit("message", "hello this is the first connection");
                        Toast.makeText(MainActivity.this,
                               "We are connected!!!", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(MainActivity.this,
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Log.e(TAG, "Error connecting");
                    Toast.makeText(MainActivity.this,
                            "Error Connecting" + String.valueOf(args[0]), Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewLink = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String link;
                    try {
                        link = data.getString("link");
                        // message = data.getString("message");
                        Toast.makeText(MainActivity.this,
                            String.valueOf(link), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        // Log.e("OnnneLinkEroor", e.getMessage());fgf
                        return;
                    }
                }
            });
        }
    };



    public void onClick(View src) {  
        switch (src.getId()) {  
            case R.id.buttonStart:  
  
                startService(new Intent(this, MyService.class));  
                break;  
            case R.id.buttonStop:  
                stopService(new Intent(this, MyService.class));  
                break;  
            case R.id.buttonNext:  
                Intent intent=new Intent(this,NextPage.class);  
                startActivity(intent);  
                break;  
        }  
    }  
}  

