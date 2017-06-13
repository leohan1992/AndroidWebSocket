package xyz.leohan.websocketlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.handshake.ServerHandshake;

import static xyz.leohan.websocketlib.WebSocketService.INTENT_DATA_URI;

/**
 * Created by leo on 2017/6/13.
 */

public class WebSocketAndroidClient {
    private Context mContext;
    private WebSocketService.MyWebSocketServiceBinder binder;
    private String uri;

    public static final String INTENT_WEBSOCKET_MSG = "webSocketMsg";
    private WebSocketConnectionListener mListener = new WebSocketConnectionListener() {
        @Override
        public void onOpen(ServerHandshake handshakedata) {

        }

        @Override
        public void onMessage(String message) {
            Intent intent = new Intent();
            intent.setAction(WebSocketReceiver.INTENT_ACTION_WEBSOCKET);
            intent.putExtra(INTENT_WEBSOCKET_MSG, message);
            mContext.sendBroadcast(intent);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            if (binder != null) {
                binder.connect();
            }
        }

        @Override
        public void onError(Exception ex) {
            Log.i("websocket",ex.getMessage());
            if (binder != null) {
                binder.connect();
            }
        }
    };

    public WebSocketAndroidClient(Builder builder) {
        mContext = builder.mContext;
        uri = builder.mUri;
    }

    public void init() {
        Intent intent = new Intent(mContext, WebSocketService.class);
        intent.putExtra(INTENT_DATA_URI, uri);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (WebSocketService.MyWebSocketServiceBinder) service;
                binder.setConnectListener(mListener);
                binder.connect();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder.close();
            }
        };
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public static class Builder {
        private String mUri;
        private Context mContext;

        public Builder setUri(String uri) {
            mUri = uri;
            return this;
        }

        public WebSocketAndroidClient build(Context context) {
            mContext = context;
            return new WebSocketAndroidClient(this);
        }
    }

}
