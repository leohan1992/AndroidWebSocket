package xyz.leohan.websocketlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by leo on 2017/6/13.
 */

public abstract class WebSocketReceiver extends BroadcastReceiver {
    public static final String INTENT_ACTION_WEBSOCKET = "xyz.leohan.Broadcase.WebSocket";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra(WebSocketAndroidClient.INTENT_WEBSOCKET_MSG);
        onMessage(msg);
    }

    public abstract void onMessage(String msg);

}
