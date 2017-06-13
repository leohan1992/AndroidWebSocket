package xyz.leohan.androidwebsocket;

import android.util.Log;

import xyz.leohan.websocketlib.WebSocketReceiver;

/**
 * Created by leo on 2017/6/13.
 */

public class MyReceiver extends WebSocketReceiver {
    @Override
    public void onMessage(String msg) {
        //Here is message from webSocket
        //you can deal it with eventBus、RxBus、Notification and so on
        Log.i("webSocket", msg);
    }
}
