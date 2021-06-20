package info.camposha.emptyjava;

import info.camposha.emptyjava.Constants;
import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class MyApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}