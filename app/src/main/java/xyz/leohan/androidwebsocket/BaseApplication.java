package xyz.leohan.androidwebsocket;

import android.app.Application;

import org.java_websocket.handshake.ServerHandshake;

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
        WebSocketAndroidClient.init(this,"ws://192.168.1.151:9898");
        WebSocketAndroidClient.getInstance().connect(new WebSocketAndroidClient.onWebSocketOpenListener() {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                try {
                    WebSocketAndroidClient.getInstance().sendMsg("connname:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IntcIkNsaWVudFR5cGVcIjpcIkFORFJPSURcIixcIkFjY291bnRcIjpcIndhbmdtZWlxaVwiLFwiUGFzc3dvcmRcIjpcImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlXCIsXCJUaW1lc3RhbXBcIjoxNTA3NjE3NjM3fSI.g6woYj2zU-7Ff8ehLdBcI0gSQNblsSTVV9IRzYl_UKY");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
