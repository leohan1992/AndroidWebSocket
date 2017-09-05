package xyz.leohan.androidwebsocket;

import android.app.Application;

import xyz.leohan.websocketlib.WebSocketAndroidClient;

/**
 * Created by leo on 2017/6/13.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //do this in your BaseApplication or MainActivity
        //Make sure you called this only once
        WebSocketAndroidClient.init(this,"ws://192.168.1.108:9898");
        WebSocketAndroidClient.getInstance().connect();
    }
}
