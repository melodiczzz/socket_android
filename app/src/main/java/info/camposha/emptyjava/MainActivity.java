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

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
  
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

        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        // perform the user login attempt.
        // mSocket.emit("okay", username);
        mSocket.on("newLink", onNewLink);
        // mSocket.on

        // mSocket.emit("run full time", username);
        // mSocket.on("login", onLogin);
        // mSocket.emit("add user", username);
        // mSocket.on("login", onLogin);
  
  
    }  

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // mSocket.off("login", onLogin);
    }

    // private Emitter.Listener onLogin = new Emitter.Listener() {
    //     @Override
    //     public void call(Object... args) {
    //         JSONObject data = (JSONObject) args[0];

    //         int numUsers;
    //         try {
    //             numUsers = data.getInt("numUsers");
    //         } catch (JSONException e) {
    //             return;
    //         }

    //         Intent intent = new Intent();
    //         intent.putExtra("username", mUsername);
    //         intent.putExtra("numUsers", numUsers);
    //         setResult(RESULT_OK, intent);
    //         finish();
    //     }
    // };


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null!=mUsername)
                            mSocket.emit("message", "hello this is the first connection");
                        Toast.makeText(getActivity().getApplicationContext(),
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error Connecting", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewLink = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String link;
                    try {
                        link = data.getString("link");
                        // message = data.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                            String.valueOf(link), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
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

